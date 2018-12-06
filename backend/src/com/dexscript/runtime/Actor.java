package com.dexscript.runtime;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Result {

    private final Scheduler scheduler;
    private boolean finished;
    private Object ret;
    private List<Actor> consumers;

    public Actor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public boolean finished() {
        return finished;
    }

    protected final void finish(Object ret) {
        finished = true;
        this.ret = ret;
        if (consumers != null) {
            for (Actor consumer : consumers) {
                scheduler.wakeUp(this, consumer);
            }
        }
    }

    @Override
    public Object value() {
        return ret;
    }

    public void addConsumer(Actor consumer) {
        if (consumers == null) {
            consumers = new ArrayList<>();
        }
        consumers.add(consumer);
    }

    public void resume() {
    }
}
