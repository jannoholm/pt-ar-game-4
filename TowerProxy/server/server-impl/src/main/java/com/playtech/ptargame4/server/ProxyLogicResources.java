package com.playtech.ptargame4.server;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame.common.task.TaskFactory;
import com.playtech.ptargame4.server.ai.GameLog;
import com.playtech.ptargame4.server.database.DatabaseAccess;
import com.playtech.ptargame4.server.registry.GameRegistry;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;

public interface ProxyLogicResources extends LogicResources {
    CallbackHandler getCallbackHandler();
    MessageParser getMessageParser();
    ProxyClientRegistry getClientRegistry();
    GameRegistry getGameRegistry();
    TaskFactory getTaskFactory();
    DatabaseAccess getDatabaseAccess();
    GameLog getGamelog();
}
