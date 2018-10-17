package com.playtech.ptargame.common.io.separator;

import java.nio.ByteBuffer;

public interface Decoder {

    boolean decode(ByteBuffer src, ByteBuffer dst);

}
