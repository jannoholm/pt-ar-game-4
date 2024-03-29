package com.playtech.ptargame4.server.session;

import com.playtech.ptargame4.api.game.GameUpdateBroadcastMessage;
import com.playtech.ptargame4.api.game.GameUpdateMessage;
import com.playtech.ptargame4.api.general.JoinServerRequest;
import com.playtech.ptargame4.api.general.PingRequest;
import com.playtech.ptargame4.api.general.PingResponse;
import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.io.Connection;
import com.playtech.ptargame.common.message.Message;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.session.Session;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame.common.task.TaskFactory;
import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.server.ContextConstants;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;
import com.playtech.ptargame4.server.task.MessageTaskInput;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSession implements Session {

    private static final Logger logger = Logger.getLogger(ClientSession.class.getName());

    private static final long PING_INTERVAL = 10000;
    private static final long PING_TIMEOUT = 2000;
    private static final int FORMAT_BUFFER_SIZE = 16384;

    private static final ThreadLocal<ByteBuffer> formatBuffer = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(FORMAT_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN));

    private final Connection connection;
    private final MessageParser parser;
    private final CallbackHandler callbackHandler;
    private final ProxyClientRegistry clientRegistry;
    private final ClientListener clientListener;
    private final TaskFactory taskFactory;

    private long lastPingSent;
    private long lastPingReceived;
    private long lastPingMessageId;
    private int pingIntervalRandom;

    private boolean authenticated = false;
    protected String clientId;
    private int userId;

    public ClientSession(Connection connection, MessageParser parser, CallbackHandler callbackHandler, ProxyClientRegistry clientRegistry, ClientListener clientListener, TaskFactory taskFactory) {
        this.connection = connection;
        this.parser = parser;
        this.callbackHandler = callbackHandler;
        this.clientRegistry = clientRegistry;
        this.clientListener = clientListener;
        this.taskFactory = taskFactory;
        this.lastPingSent = System.currentTimeMillis();
        this.lastPingReceived = this.lastPingSent;
        this.pingIntervalRandom = this.connection.getConnectionId() & 0x7FFFFFFF;
        for (int i = 0; i < 100 && this.pingIntervalRandom>0x3FF; ++i ) {
            this.pingIntervalRandom = this.pingIntervalRandom >> 1;
        }
    }

    @Override
    public int getId() {
        return this.connection.getConnectionId();
    }

    public void setAuthenticated(int userId) {
        if (!authenticated) {
            this.userId = userId;
            logger.log(Level.INFO, () -> String.format(" %6s Identified as client: %s, and user: %s", this.connection.getConnectionId(), this.clientId, this.userId));
        }
    }

    public void publishSession() {
        if (this.clientListener != null) {
            this.clientListener.clientConnected(this.clientId, this.userId);
        }
    }

    public String getClientId() {
        return clientId;
    }

    public int getUserId() {
        return userId;
    }

    public void processMessage(List<ByteBuffer> messageBytes) {
        Message message = this.parser.parseMessage(messageBytes);
        if (isNoisyRequest(message)) {
            logger.log(Level.FINER, ()->String.format(" %6s %3s: %s", this.connection.getConnectionId(), "IN", message));
        } else {
            logger.log(Level.FINE, ()->String.format(" %6s %3s: %s", this.connection.getConnectionId(), "IN", message));
        }
        processMessage(message);
    }

    protected void processMessage(Message message) {
        if (message instanceof PingRequest) {
            processPingRequest((PingRequest)message);
        } else if (message instanceof PingResponse) {
            processPingResponse(message);
        } else if (!authenticated && message instanceof JoinServerRequest) {
            processJoinServer((JoinServerRequest)message);
        } else if (message.getClass().getSimpleName().endsWith("Response")) {
            processResponse(message);
        } else {
            processRequest(message);
        }
    }

    public void sendMessage(Message message) {
        if (isNoisyRequest(message)) {
            logger.log(Level.FINER, ()->String.format(" %6s %3s: %s", this.connection.getConnectionId(), "OUT", message));
        } else {
            logger.log(Level.FINE, ()->String.format(" %6s %3s: %s", this.connection.getConnectionId(), "OUT", message));
        }
        ByteBuffer messageBytes = formatBuffer.get();
        messageBytes.clear();
        this.parser.formatMessage(message, messageBytes);
        messageBytes.flip();
        this.connection.write(Collections.singletonList(messageBytes));
    }

    public void ping(long time) {
        if (this.lastPingSent > this.lastPingReceived && this.lastPingSent+PING_TIMEOUT<time){
            // timeout. close connection
            logger.info(String.format(" %6s %s", this.connection.getConnectionId(), " Ping failed. Timeout passed."));
            this.connection.close();
        }
        if (this.lastPingSent <= lastPingReceived && this.lastPingSent+PING_INTERVAL+this.pingIntervalRandom < time){
            // send ping
            PingRequest request = parser.createMessage(PingRequest.class);
            this.lastPingMessageId = request.getHeader().getMessageId();
            sendMessage(request);
            this.lastPingSent=time;
        }
    }

    @Override
    public void close() {
        this.connection.close();
    }

    @Override
    public void cleanup() {
        this.clientRegistry.removeClientConnection(this.clientId, this);
        if (this.clientListener != null) {
            this.clientListener.clientDisconnected(this.clientId, this.userId);
        }
    }

    private void processPingRequest(PingRequest request) {
        PingResponse response = this.parser.createResponse(request, PingResponse.class);
        sendMessage(response);
    }

    private void processPingResponse(Message message) {
        if ( this.lastPingMessageId == message.getHeader().getMessageId() ) {
            this.lastPingReceived = System.currentTimeMillis();
            logger.info(String.format(" %6s Ping time: %sms", this.connection.getConnectionId(), (lastPingReceived - lastPingSent)));
        }
    }

    private void processRequest(Message message) {
        Task task = this.taskFactory.getTask(new MessageTaskInput(message));
        task.getContext().put(ContextConstants.CONNECTION, this);
        task.scheduleExecution();
    }

    private void processResponse(Message message) {
        this.callbackHandler.addResponse(message);
    }

    private void processJoinServer(JoinServerRequest request) {
        if (StringUtil.isNull(request.getHeader().getClientId())) {
            request.getHeader().setClientId(UUID.randomUUID().toString());
        }
        this.clientId = request.getHeader().getClientId();
        processRequest(request);
    }

    private boolean isNoisyRequest( Message message ) {
        return message instanceof GameUpdateMessage || message instanceof GameUpdateBroadcastMessage;
    }
}
