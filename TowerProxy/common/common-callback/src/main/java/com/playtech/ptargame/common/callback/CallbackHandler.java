package com.playtech.ptargame.common.callback;


import com.playtech.ptargame.common.message.Message;
import com.playtech.ptargame.common.session.Session;
import com.playtech.ptargame.common.task.Task;

public interface CallbackHandler {
    void sendMessage(Message message);
    void sendCallback(Task task, Message request);
    void sendCallback(Task task, Message request, Session session);
    void addResponse(Message response);
    ResponseStatus getResponseStatus(Task task, Message request);
    Message getResponse(Task task, Message request);
    enum ResponseStatus{
        PENDING,
        SUCCESS,
        UNROUTABLE,
        TIMEOUT
    }
}
