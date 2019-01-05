package com.dexscript.type.core;

import com.dexscript.type.core.Invocation;
import com.dexscript.type.core.NamedArg;
import com.dexscript.type.core.TypeSystem;
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
        Assert.assertEquals("hello(): string", new Invocation(
                "hello", null,
                null, null, ts.STRING).toString());
        Assert.assertEquals("hello<string>(): string", new Invocation(
                "hello", Arrays.asList(ts.STRING),
                null, null, ts.STRING).toString());
        Assert.assertEquals("hello(string): string", new Invocation(
                "hello", null,
                Arrays.asList(ts.STRING), null, ts.STRING).toString());
        Assert.assertEquals("hello(msg=string): string", new Invocation(
                "hello", null,
                null, Arrays.asList(new NamedArg("msg", ts.STRING)), ts.STRING).toString());
    }
}
