package com.playtech.ptargame4.server.task.game;

import com.playtech.ptargame4.api.game.GameControlMessage;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.server.registry.GameRegistryGame;
import com.playtech.ptargame4.server.task.AbstractLogic;


public class GameControlLogic extends AbstractLogic {
    public GameControlLogic(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public void execute(Task task) {
        // message from client
        GameControlMessage inMessage = getInputMessage(task, GameControlMessage.class);

        // get and validate game
        GameRegistryGame game = getLogicResources().getGameRegistry().getGame(inMessage.getGameId());

        // create message to push to host
        GameControlMessage toHost = getLogicResources().getMessageParser().createMessage(GameControlMessage.class);
        toHost.getHeader().setClientId(game.getHostClientId());
        toHost.setGameId(game.getGameId());
        toHost.setControlClientId(inMessage.getHeader().getClientId());
        toHost.setControlData(inMessage.getControlData());

        // send message
        getLogicResources().getCallbackHandler().sendMessage(toHost);
    }
}
