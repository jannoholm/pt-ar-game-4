package com.playtech.ptargame.common.message;

import java.nio.ByteBuffer;

public interface Message {

    MessageHeader getHeader();

    void parse(ByteBuffer messageData);

    void format(ByteBuffer messageData);

}
