package com.dexscript.observability;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EventTest {

    @Before
    public void setup() {
        Observability.SPI.reset();
    }

    public interface MyInf1 {
        void hello();
    }

    @Test
    public void interface_without_arg() {
        List<Event> events = new ArrayList<>();
        Observability.SPI.registerEventHandler(events::add);
        MyInf1 inf = Observability.instrument(MyInf1.class);
        inf.hello();
        Assert.assertEquals(1, events.size());
        Assert.assertArrayEquals(new String[0], events.get(0).argNames);
        Assert.assertArrayEquals(new String[0], events.get(0).argValues);
    }

    public interface MyInf2 {
        void hello(String a, int b);
    }

    @Test
    public void interface_with_2_args() {
        List<Event> events = new ArrayList<>();
        Observability.SPI.registerEventHandler(events::add);
        MyInf2 inf = Observability.instrument(MyInf2.class);
        inf.hello("hello", 100);
        Assert.assertEquals(1, events.size());
        Assert.assertArrayEquals(new String[]{"a", "b"}, events.get(0).argNames);
        Assert.assertArrayEquals(new String[]{"hello", "100"}, events.get(0).argValues);
    }
}
