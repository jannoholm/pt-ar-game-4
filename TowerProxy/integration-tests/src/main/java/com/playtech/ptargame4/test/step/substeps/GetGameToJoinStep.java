package com.playtech.ptargame4.test.step.substeps;


import com.playtech.ptargame4.api.AbstractRequest;
import com.playtech.ptargame4.api.AbstractResponse;
import com.playtech.ptargame4.api.lobby.GetGamesRequest;
import com.playtech.ptargame4.api.lobby.GetGamesResponse;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame.common.task.TaskState;
import com.playtech.ptargame.common.task.state.TwoStepState;
import com.playtech.ptargame4.test.ContextConstants;
import com.playtech.ptargame4.test.step.common.SimpleCallbackStep;

import java.util.Random;

public class GetGameToJoinStep extends SimpleCallbackStep {

    private Random random = new Random(System.currentTimeMillis());

    public GetGameToJoinStep(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public TaskState initialState() {
        return TwoStepState.INITIAL;
    }

    @Override
    protected AbstractRequest createRequest(Task task) {
        GetGamesRequest request = createMessage(task, GetGamesRequest.class);
        request.setAll(false);
        return request;
    }

    @Override
    protected void processResponse(Task task, AbstractResponse response) {
        GetGamesResponse getGamesResponse = (GetGamesResponse)response;
        if (getGamesResponse.getGames().size() > 0) {
            task.getContext().put(ContextConstants.JOIN_GAME_ID, getGamesResponse.getGames().get(random.nextInt(getGamesResponse.getGames().size())).getGameId());
        }
    }

}
