package com.playtech.ptargame4.examplebot.logic.table;

import com.playtech.ptargame4.api.game.GameUpdateMessage;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.examplebot.logic.AbstractBotLogic;

import java.util.logging.Logger;

public class GameUpdateLogic extends AbstractBotLogic {

    private static final Logger logger = Logger.getLogger(GameUpdateLogic.class.getName());

    public GameUpdateLogic(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public void execute(Task task) {
        GameUpdateMessage message = getInputMessage(task, GameUpdateMessage.class);
        logger.fine("TODO - got update: " + message.getGameId());
    }
}
