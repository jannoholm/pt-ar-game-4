package com.playtech.ptargame4.examplebot.logic;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.callback.ClientRegistry;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame4.examplebot.scenario.SleepManager;

public interface BotLogicResources extends LogicResources {
    CallbackHandler getCallbackHandler();

    MessageParser getMessageParser();

    ClientRegistry getClientRegistry();

    SleepManager getSleepManager();
}
