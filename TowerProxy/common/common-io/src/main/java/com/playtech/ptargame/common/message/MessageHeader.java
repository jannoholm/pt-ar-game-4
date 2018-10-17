package com.playtech.ptargame.common.message;


public interface MessageHeader {
    int getMessageType();
    long getMessageId();
    String getClientId();
    void setClientId(String clientId);
}
