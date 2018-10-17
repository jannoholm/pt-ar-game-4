package com.playtech.ptargame4.test.scenario;


import com.playtech.ptargame4.api.general.JoinServerRequest;
import com.playtech.ptargame.common.task.Logic;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame.common.task.state.TwoStepState;
import com.playtech.ptargame4.test.step.GetGameToJoinWithWaitStep;
import com.playtech.ptargame4.test.step.JoinGameStep;
import com.playtech.ptargame4.test.step.JoinServerStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinGameScenario extends AbstractScenario {

    public JoinGameScenario(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public List<Logic> createStateSubLogics(Task context) {
        if (context.getCurrentState() == TwoStepState.FINAL) {
            ArrayList<Logic> logics = new ArrayList<>();
            logics.add(new JoinServerStep(getLogicResources(), JoinServerRequest.ClientType.GAME_CLIENT));
            logics.add(new GetGameToJoinWithWaitStep(getLogicResources()));
            logics.add(new JoinGameStep(getLogicResources()));
            //logics.add(new WaitGameStartStep(getLogicResources()));
            return logics;
        } else {
            return Collections.emptyList();
        }
    }

}
