package com.playtech.ptargame4.test.step.common;

import com.playtech.ptargame.common.message.Message;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.test.logic.AbstractTestLogic;
import com.playtech.ptargame4.test.ContextConstants;

public abstract class AbstractStep extends AbstractTestLogic {

    public AbstractStep(LogicResources logicResources) {
        super(logicResources);
    }

    protected <T extends Message> T createMessage(Task task, Class<T> messageClass) {
        String clientId = task.getContext().get(ContextConstants.CLIENT_ID, String.class);
        T message = getLogicResources().getMessageParser().createMessage(messageClass);
        message.getHeader().setClientId(clientId);
        return message;
    }
}
