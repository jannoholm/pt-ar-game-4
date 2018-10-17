package com.playtech.ptargame.common.task;

public interface TaskState {
    TaskState next();
    boolean isFinal();
}
