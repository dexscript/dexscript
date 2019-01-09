package com.dexscript.observability;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EventTest {

    public interface MyInf {
        void hello();
    }

    @Before
    public void setup() {
        Observability.SPI.reset();
    }

    @Test
    public void send_event_via_interface() {
        List<Event> events = new ArrayList<>();
        Observability.SPI.registerEventHandler(events::add);
        MyInf inf = Observability.instrument(MyInf.class);
        inf.hello();
        Assert.assertEquals(1, events.size());
    }
}
