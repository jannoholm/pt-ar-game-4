package com.playtech.ptargame4.test;

import com.playtech.ptargame.common.callback.CallbackHandler;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.LogicResources;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;
import com.playtech.ptargame4.test.registry.GameRegistryStub;
import com.playtech.ptargame4.test.registry.TestSleepManager;


public interface TestLogicResources extends LogicResources {
    CallbackHandler getCallbackHandler();
    MessageParser getMessageParser();
    ProxyClientRegistry getClientRegistry();
    GameRegistryStub getGameRegistryStub();
    TestSleepManager getTestSleepManager();
}
