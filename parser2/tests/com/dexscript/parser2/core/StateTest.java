package com.dexscript.parser2.core;

import org.junit.Assert;
import org.junit.Test;

public class StateTest {

    @Test
    public void one_state() {
        State.Play(() -> null);
    }

    @Test
    public void a_to_b() {
        final boolean[] reached = new boolean[1];
        State b = () -> {
            reached[0] = true;
            return null;
        };
        State a = () -> b;
        State.Play(a);
        Assert.assertTrue(reached[0]);
    }
}
