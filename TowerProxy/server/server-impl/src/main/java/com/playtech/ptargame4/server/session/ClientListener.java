package com.playtech.ptargame4.server.session;


public interface ClientListener {

    void clientConnected(String clientId, int userId);
    void clientDisconnected(String clientId, int userId);

}
