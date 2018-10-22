package com.playtech.ptargame.common.callback;


import com.playtech.ptargame.common.session.Session;

import java.util.Collection;

public interface ClientRegistry {
    Collection<Session> getClientSession(String clientId);
    String getName(String clientId);
    Collection<Session> getTableSessions();
}
