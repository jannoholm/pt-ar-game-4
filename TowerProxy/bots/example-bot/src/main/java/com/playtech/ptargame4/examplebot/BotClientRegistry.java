package com.playtech.ptargame4.examplebot;

import com.playtech.ptargame.common.callback.ClientRegistry;
import com.playtech.ptargame.common.session.Session;

import java.util.Collection;
import java.util.Collections;

public class BotClientRegistry implements ClientRegistry, ConnectivityListener {
    @Override
    public Collection<Session> getClientSession(String clientId) {
        return null;
    }

    @Override
    public String getName(String clientId) {
        return null;
    }

    @Override
    public Collection<Session> getTableSessions() {
        return Collections.emptyList();
    }

    @Override
    public void clientDisconnected(String clientId) {
        // ConnectivityListener method. remove client
    }
}
