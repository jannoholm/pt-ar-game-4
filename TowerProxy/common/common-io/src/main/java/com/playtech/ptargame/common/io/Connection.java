package com.playtech.ptargame.common.io;

import java.nio.ByteBuffer;
import java.util.List;

public interface Connection {

    void close();

    void write(List<ByteBuffer> message);

    int getConnectionId();

}
