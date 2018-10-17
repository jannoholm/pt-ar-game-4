package com.playtech.ptargame4.server.task.game;


import com.playtech.ptargame4.api.table.CarControlMessage;
import com.playtech.ptargame.common.session.Session;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.Task;
import com.playtech.ptargame4.server.task.AbstractLogic;

public class CarControlLogic extends AbstractLogic {
    public CarControlLogic(LogicResources logicResources) {
        super(logicResources);
    }

    @Override
    public void execute(Task task) {
        // message from client
        CarControlMessage inMessage = getInputMessage(task, CarControlMessage.class);

        // get and validate game
        for (Session session : getLogicResources().getClientRegistry().getCarControlSessions()) {
            session.sendMessage(inMessage);
        }
    }
}
