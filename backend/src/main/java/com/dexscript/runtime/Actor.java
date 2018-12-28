package com.dexscript.runtime;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Promise, Task {

    protected final Scheduler scheduler;
    protected final Object context;
    private boolean finished;
    private Object ret;
    private List<Actor> consumers;
    private int state;
    private int yieldToState = -1;

    public Actor(Scheduler scheduler, Object context) {
        this.scheduler = scheduler;
        this.context = context;
    }

    @Override
    public boolean finished() {
        return finished;
    }

    public final void produce(Object ret) {
        finished = true;
        this.ret = ret;
        if (consumers != null) {
            for (Actor consumer : consumers) {
                scheduler.wakeUp(this, consumer);
            }
        }
    }

    protected final void yieldTo(int yieldToState) {
        this.yieldToState = yieldToState;
        scheduler.wakeUp(this, this);
    }

    protected final int resetYield() {
        int state = yieldToState;
        yieldToState = -1;
        return state;
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
