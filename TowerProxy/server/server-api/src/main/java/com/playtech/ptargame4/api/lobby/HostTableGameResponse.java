package com.playtech.ptargame4.api.lobby;

import java.nio.ByteBuffer;

import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.api.AbstractResponse;

public class HostTableGameResponse extends AbstractResponse {

    private String gameId;

    public HostTableGameResponse(MessageHeader header) {
        super(header);
    }

    @Override
    protected void toStringImpl(StringBuilder s) {
        super.toStringImpl(s);
        s.append(", gameId=").append(getGameId());
    }

    @Override
    public void parse(ByteBuffer messageData) {
        super.parse(messageData);
        this.gameId = StringUtil.readUTF8String(messageData);
    }

    @Override
    public void format(ByteBuffer messageData) {
        super.format(messageData);
        StringUtil.writeUTF8String(this.gameId, messageData);
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
