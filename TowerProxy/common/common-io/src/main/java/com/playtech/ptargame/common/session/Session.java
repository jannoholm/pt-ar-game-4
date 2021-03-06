package com.playtech.ptargame.common.session;

import com.playtech.ptargame.common.message.Message;

import java.nio.ByteBuffer;
import java.util.List;

public interface Session {

    int getId();

    void processMessage(List<ByteBuffer> message);

    void sendMessage(Message message);

    void ping(long time);

    void close();

    void cleanup();

}
