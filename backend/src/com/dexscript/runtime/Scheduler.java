package com.dexscript.runtime;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<Actor> readyActors = new ArrayList<>();

    public final void schedule() {
        while (!readyActors.isEmpty()) {
            List<Actor> todoActors = readyActors;
            readyActors = new ArrayList<>();
            for (Actor todoActor : todoActors) {
                todoActor.resume();
            }
        }
    }

    final void wakeUp(Actor producer, Actor consumer) {
        System.out.println("wake up: " + producer + " => " + consumer);
        readyActors.add(consumer);
    }
}
