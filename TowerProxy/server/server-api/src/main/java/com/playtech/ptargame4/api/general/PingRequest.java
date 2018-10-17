package com.playtech.ptargame4.api.general;

import com.playtech.ptargame4.api.AbstractMessage;
import com.playtech.ptargame.common.message.MessageHeader;

public class PingRequest extends AbstractMessage {

    public PingRequest(MessageHeader header) {
        super(header);
    }

}
