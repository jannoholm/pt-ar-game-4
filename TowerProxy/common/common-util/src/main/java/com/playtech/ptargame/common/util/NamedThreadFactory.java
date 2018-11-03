package com.playtech.ptargame.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger count = new AtomicInteger(0);
    private final String prefix;
    private final boolean isDaemon;

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean isDaemon) {
        this.prefix = prefix;
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, prefix + "-" + count.addAndGet(1));
        thread.setDaemon(isDaemon);
        return thread;
    }
}
