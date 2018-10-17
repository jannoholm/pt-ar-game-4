package com.playtech.ptargame4.api.table;


import com.playtech.ptargame4.api.AbstractRequest;
import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.StringUtil;

import java.nio.ByteBuffer;

public class GetUsersRequest extends AbstractRequest {

    private String filter;

    public GetUsersRequest(MessageHeader header) {
        super(header);
    }

    public void parse(ByteBuffer messageData) {
        super.parse(messageData);
        filter = StringUtil.readUTF8String(messageData);
    }

    public void format(ByteBuffer messageData) {
        super.format(messageData);
        StringUtil.writeUTF8String(filter, messageData);
    }

    protected void toStringImpl(StringBuilder s) {
        super.toStringImpl(s);
        s.append(", filter=").append(filter);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
