package com.playtech.ptargame4.examplebot.scenario.steps;

import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.examplebot.logic.ContextConstants;

public class SetBotNameStep extends AbstractStep {

    private String botName;

    public SetBotNameStep(LogicResources logicResources, String botName) {
        super(logicResources);
        this.botName = botName;
    }

    @Override
    public void execute(Task task) {
        task.getContext().put(ContextConstants.CLIENT_NAME, botName);
    }
}
