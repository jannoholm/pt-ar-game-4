package com.playtech.ptargame4.server;

import com.playtech.ptargame4.api.ProxyMessageFactory;
import com.playtech.ptargame4.api.ProxyMessageParser;
import com.playtech.ptargame.common.callback.CallbackHandlerImpl;
import com.playtech.ptargame.common.io.NioServerListener;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.task.TaskExecutorImpl;
import com.playtech.ptargame.common.task.TaskFactory;
import com.playtech.ptargame.common.task.TaskFactoryImpl;
import com.playtech.ptargame4.server.ai.GameLogImpl;
import com.playtech.ptargame4.server.conf.ConfigurationImpl;
import com.playtech.ptargame4.server.database.DatabaseAccessImpl;
import com.playtech.ptargame4.server.registry.GameRegistry;
import com.playtech.ptargame4.server.registry.ProxyClientRegistry;
import com.playtech.ptargame4.server.registry.ProxyLogicRegistry;
import com.playtech.ptargame.common.util.NamedThreadFactory;
import com.playtech.ptargame4.server.task.pub.registration.UserPollerImpl;
import com.playtech.ptargame4.server.web.WebListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Starter {

    private static final Starter starter = new Starter();

    private NioServerListener proxyListener;
    private WebListener webListener;
    private TaskExecutorImpl taskExecutor;

    private void run() throws Exception {
        ProxyMessageFactory messageFactory = new ProxyMessageFactory();
        messageFactory.initialize();
        MessageParser messageParser = new ProxyMessageParser(messageFactory);
        ProxyClientRegistry clientRegistry = new ProxyClientRegistry();
        ScheduledExecutorService backgroundExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("back", true));
        ConfigurationImpl configuration = new ConfigurationImpl(backgroundExecutorService);
        configuration.init();
        CallbackHandlerImpl callbackHandler = new CallbackHandlerImpl(clientRegistry, backgroundExecutorService);
        callbackHandler.start();
        taskExecutor = new TaskExecutorImpl("te", 2);
        ProxyLogicRegistry logicRegistry = new ProxyLogicRegistry();
        TaskFactory taskFactory = new TaskFactoryImpl(taskExecutor, logicRegistry);
        GameRegistry gameRegistry = new GameRegistry(backgroundExecutorService);
        DatabaseAccessImpl databaseAccess = new DatabaseAccessImpl(backgroundExecutorService);
        databaseAccess.setup();
        GameLogImpl gameLog = new GameLogImpl(backgroundExecutorService);
        gameLog.init();
        LogicResourcesImpl logicResources = new LogicResourcesImpl(callbackHandler, messageParser, clientRegistry, gameRegistry, taskFactory, databaseAccess, gameLog);
        logicRegistry.initialize(logicResources);
        ProxyConnectionFactory connectionFactory = new ProxyConnectionFactory(messageParser, callbackHandler, clientRegistry, gameRegistry, taskFactory);

        proxyListener = new NioServerListener(connectionFactory, configuration.getBinaryPort());
        new Thread(proxyListener.start(), "proxyListener").start();

        webListener = new WebListener(databaseAccess, clientRegistry, messageParser, configuration);
        webListener.start();

        ScheduledExecutorService pollerExecutorService = Executors.newScheduledThreadPool(2, new NamedThreadFactory("poll", true));
        UserPollerImpl poller = new UserPollerImpl(pollerExecutorService, configuration, databaseAccess);
        poller.init();

    }

    private void stop() {
        if (proxyListener != null) {
            proxyListener.stop();
        }
        if (webListener != null) {
            webListener.stop();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignore) {}
        taskExecutor.stop();
    }

    public static void main(String[] args) throws Exception {
        start(args);
    }

    public static void start(String[] args) throws Exception {
        starter.run();
    }
    public static void stop(String[] args) throws Exception {
        starter.stop();
    }
}
