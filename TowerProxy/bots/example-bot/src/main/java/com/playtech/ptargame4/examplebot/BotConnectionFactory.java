package com.playtech.ptargame4.examplebot;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.io.ConnectionFactory;
import com.playtech.ptargame.common.io.ConnectionHandler;
import com.playtech.ptargame.common.io.separator.Decoder;
import com.playtech.ptargame.common.io.separator.Encoder;
import com.playtech.ptargame.common.io.separator.LengthDecoder;
import com.playtech.ptargame.common.io.separator.LengthEncoder;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.TaskFactory;

public class BotConnectionFactory implements ConnectionFactory {
    private static final int MESSAGE_LIMIT = 16384;

    private final MessageParser messageParser;
    private final CallbackHandler callbackHandler;
    private final ConnectivityListener connectivityListener;
    private final TaskFactory taskFactory;

    public BotConnectionFactory(MessageParser messageParser, CallbackHandler callbackHandler, ConnectivityListener connectivityListener, TaskFactory taskFactory) {
        this.messageParser = messageParser;
        this.callbackHandler = callbackHandler;
        this.connectivityListener = connectivityListener;
        this.taskFactory = taskFactory;
    }

    @Override
    public ConnectionHandler createConnection() {
        Encoder encoder = new LengthEncoder(MESSAGE_LIMIT);
        Decoder decoder = new LengthDecoder(MESSAGE_LIMIT);
        ConnectionHandler handler = new ConnectionHandler(encoder, decoder);
        BotSession session = new BotSession(handler, this.messageParser, this.callbackHandler, connectivityListener, taskFactory);
        handler.setSession(session);
        return handler;
    }

}
