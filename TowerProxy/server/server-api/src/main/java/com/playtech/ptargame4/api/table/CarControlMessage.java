package com.playtech.ptargame4.api.table;

import com.playtech.ptargame4.api.AbstractMessage;
import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.HexUtil;

import java.nio.ByteBuffer;

public class CarControlMessage extends AbstractMessage {

    private byte[] controlData;

    public CarControlMessage(MessageHeader header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer messageData) {
        int size = messageData.getInt();
        controlData = new byte[size];
        messageData.get(controlData);
    }

    @Override
    public void format(ByteBuffer messageData) {
        if (controlData != null) {
            messageData.putInt(controlData.length);
            messageData.put(controlData);
        } else {
            messageData.putInt(0);
        }
    }

    @Override
    protected void toStringImpl(StringBuilder s) {
        super.toStringImpl(s);
        s.append(", controlData=").append(HexUtil.toHex(controlData));
    }

    public byte[] getControlData() {
        return controlData;
    }

    public void setControlData(byte[] controlData) {
        this.controlData = controlData;
    }
}
