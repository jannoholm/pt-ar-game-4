package com.playtech.ptargame4.test;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.io.Connection;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.TaskFactory;
import com.playtech.ptargame4.server.registry.GameRegistry;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;
import com.playtech.ptargame4.server.session.ClientSession;


public class ConnectorSession extends ClientSession {
    public ConnectorSession(Connection connection, MessageParser parser, CallbackHandler callbackHandler, ProxyClientRegistry clientRegistry, GameRegistry gameRegistry, TaskFactory taskFactory) {
        super(connection, parser, callbackHandler, clientRegistry, gameRegistry, taskFactory);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
