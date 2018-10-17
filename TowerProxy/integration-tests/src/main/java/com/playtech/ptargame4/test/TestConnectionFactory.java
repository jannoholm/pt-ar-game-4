package com.playtech.ptargame4.test;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.io.ConnectionFactory;
import com.playtech.ptargame.common.io.ConnectionHandler;
import com.playtech.ptargame.common.io.separator.Decoder;
import com.playtech.ptargame.common.io.separator.Encoder;
import com.playtech.ptargame.common.io.separator.LengthDecoder;
import com.playtech.ptargame.common.io.separator.LengthEncoder;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.TaskFactory;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;

public class TestConnectionFactory implements ConnectionFactory {

    private static final int MESSAGE_LIMIT = 16384;

    private final MessageParser messageParser;
    private final CallbackHandler callbackHandler;
    private final ProxyClientRegistry clientRegistry;
    private final TaskFactory taskFactory;

    public TestConnectionFactory(MessageParser messageParser, CallbackHandler callbackHandler, ProxyClientRegistry clientRegistry, TaskFactory taskFactory) {
        this.messageParser = messageParser;
        this.callbackHandler = callbackHandler;
        this.clientRegistry = clientRegistry;
        this.taskFactory = taskFactory;
    }

    @Override
    public ConnectionHandler createConnection() {
        Encoder encoder = new LengthEncoder(MESSAGE_LIMIT);
        Decoder decoder = new LengthDecoder(MESSAGE_LIMIT);
        ConnectionHandler handler = new ConnectionHandler(encoder, decoder);
        ConnectorSession session = new ConnectorSession(handler, this.messageParser, this.callbackHandler, clientRegistry, null, taskFactory);
        handler.setSession(session);
        return handler;
    }

}
