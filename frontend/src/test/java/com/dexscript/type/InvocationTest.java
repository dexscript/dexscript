package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class InvocationTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void to_string() {
        Assert.assertEquals("hello($=interface{}): string", new Invocation(
                "hello", null,
                null, null, ts.ANY, ts.STRING).toString());
        Assert.assertEquals("hello<string>($=interface{}): string", new Invocation(
                "hello", Arrays.asList(ts.STRING),
                null, null, ts.ANY, ts.STRING).toString());
        Assert.assertEquals("hello(string, $=interface{}): string", new Invocation(
                "hello", null,
                Arrays.asList(ts.STRING), null, ts.ANY, ts.STRING).toString());
        Assert.assertEquals("hello(msg=string, $=interface{}): string", new Invocation(
                "hello", null,
                null, Arrays.asList(new NamedArg("msg", ts.STRING)), ts.ANY, ts.STRING).toString());
    }
}
