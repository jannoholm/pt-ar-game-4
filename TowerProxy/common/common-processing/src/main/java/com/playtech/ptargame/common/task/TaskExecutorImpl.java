package com.playtech.ptargame.common.task;


import com.playtech.ptargame.common.util.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TaskExecutorImpl implements  TaskExecutor {

    public static final Logger logger = Logger.getLogger(TaskExecutorImpl.class.getName());

    private ExecutorService executor;

    public TaskExecutorImpl(final String prefix, int threads) {
        executor = Executors.newFixedThreadPool(threads, new NamedThreadFactory(prefix));
    }

    public void addTask(Task task) {
        executor.execute(task);
    }

    public void stop() {
        executor.shutdown();
    }

}
