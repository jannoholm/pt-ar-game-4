package com.playtech.ptargame4.api;


import com.playtech.ptargame.common.message.MessageHeader;

public abstract class AbstractRequest extends AbstractMessage {
    public AbstractRequest(MessageHeader header) {
        super(header);
    }
}
