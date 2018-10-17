package com.playtech.ptargame4.api.table;


import com.playtech.ptargame4.api.AbstractMessage;
import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.HexUtil;

import java.nio.ByteBuffer;

public class LocationNotificationMessage extends AbstractMessage {

    private byte[] locationData;

    public LocationNotificationMessage(MessageHeader header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer messageData) {
        int size = messageData.getInt();
        locationData = new byte[size];
        messageData.get(locationData);
    }

    @Override
    public void format(ByteBuffer messageData) {
        if (locationData != null) {
            messageData.putInt(locationData.length);
            messageData.put(locationData);
        } else {
            messageData.putInt(0);
        }
    }

    @Override
    protected void toStringImpl(StringBuilder s) {
        super.toStringImpl(s);
        s.append(", controlData=").append(HexUtil.toHex(locationData));
    }

    public byte[] getLocationData() {
        return locationData;
    }

    public void setLocationData(byte[] locationData) {
        this.locationData = locationData;
    }
}
