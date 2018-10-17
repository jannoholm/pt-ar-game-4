package com.playtech.ptargame4.server.task;

import com.playtech.ptargame.common.message.Message;
import com.playtech.ptargame.common.task.TaskInput;
import com.playtech.ptargame.common.util.StringUtil;

public class MessageTaskInput implements TaskInput {
    private final Message message;

    public MessageTaskInput(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String getId() {
        if (StringUtil.isNull(message.getHeader().getClientId())) {
            return "ID" + message.getHeader().getMessageId();
        } else {
            return "ID" + message.getHeader().getMessageId() + "-" + message.getHeader().getClientId();
        }
    }

    @Override
    public String getType() {
        return message.getClass().getName();
    }
}
