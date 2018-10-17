package com.playtech.ptargame4.test.step.substeps;

import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.test.ContextConstants;
import com.playtech.ptargame4.test.step.common.AbstractStep;


public class RegisterForGameLobbyUpdateStep extends AbstractStep {
    public RegisterForGameLobbyUpdateStep(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public void execute(Task task) {
        String gameId = task.getContext().get(ContextConstants.PLAY_GAME_ID, String.class);
        String clientId = task.getContext().get(ContextConstants.CLIENT_ID, String.class);
        getLogicResources().getGameRegistryStub().addGameTask(gameId, clientId, task);
    }
}
