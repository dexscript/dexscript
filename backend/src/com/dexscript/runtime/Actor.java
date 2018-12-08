package com.dexscript.runtime;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Promise {

    private final Scheduler scheduler;
    private boolean finished;
    private Object ret;
    private List<Actor> consumers;
    private int state;

    public Actor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public boolean finished() {
        return finished;
    }

    public final void finish(Object ret) {
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

    public int Get__state() {
        return state;
    }

    protected void Set__state(int state) {
        this.state = state;
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
