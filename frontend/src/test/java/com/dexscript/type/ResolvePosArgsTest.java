package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ResolvePosArgsTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void empty() {
        Assert.assertEquals(0, ResolvePosArgs.$(ts, "").size());
    }

    @Test
    public void one_arg() {
        Assert.assertEquals(Arrays.asList(ts.STRING), ResolvePosArgs.$(ts, "string"));
    }

    @Test
    public void two_args() {
        Assert.assertEquals(Arrays.asList(ts.STRING, ts.STRING), ResolvePosArgs.$(ts, "string,string"));
    }
}
