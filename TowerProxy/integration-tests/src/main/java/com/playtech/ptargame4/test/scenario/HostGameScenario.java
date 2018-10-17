package com.playtech.ptargame4.test.scenario;


import com.playtech.ptargame4.api.general.JoinServerRequest;
import com.playtech.ptargame.common.task.Logic;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame.common.task.state.TwoStepState;
import com.playtech.ptargame4.test.step.ValidateHostedGameStep;
import com.playtech.ptargame4.test.step.HostGameStep;
import com.playtech.ptargame4.test.step.JoinServerStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostGameScenario extends AbstractScenario {

    public HostGameScenario(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public List<Logic> createStateSubLogics(Task context) {
        if (context.getCurrentState() == TwoStepState.FINAL) {
            ArrayList<Logic> logics = new ArrayList<>();
            logics.add(new JoinServerStep(getLogicResources(), JoinServerRequest.ClientType.GAME_CLIENT));
            logics.add(new HostGameStep(getLogicResources()));
            logics.add(new ValidateHostedGameStep(getLogicResources()));
            //logics.add(new WaitGameStartStep(getLogicResources()));
            return logics;
        } else {
            return Collections.emptyList();
        }
    }

}
