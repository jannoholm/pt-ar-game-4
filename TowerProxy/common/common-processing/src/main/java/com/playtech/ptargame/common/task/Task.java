package com.playtech.ptargame.common.task;


public interface Task extends Runnable {
    TaskState getCurrentState();
    TaskContext getContext();
    void scheduleExecution();
}
