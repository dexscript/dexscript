package com.dexscript.observability.buf;

import com.dexscript.observability.Event;

import java.util.List;
import java.util.function.Predicate;

public class DirectEventBuffer implements EventBuffer {
    @Override
    public void accept(Event event) {
    }

    @Override
    public List<Event> execute(Predicate<Event> predicate, long from, int limit) {
        return null;
    }
}
