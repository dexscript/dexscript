package com.dexscript.infer;

import com.dexscript.type.core.NamedArg;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ResolveNamedArgsTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void empty() {
        Assert.assertEquals(0, ResolveNamedArgs.$(ts, "").size());
    }

    @Test
    public void one_named_arg() {
        List<NamedArg> args = ResolveNamedArgs.$(ts, "arg1=string");
        Assert.assertEquals(1, args.size());
        Assert.assertEquals(new NamedArg("arg1", ts.STRING), args.get(0));
    }

    @Test
    public void const_type() {
        List<NamedArg> args = ResolveNamedArgs.$(ts, "arg1=(const)100");
        Assert.assertEquals(1, args.size());
        Assert.assertEquals(new NamedArg("arg1", ts.constOf(100)), args.get(0));
    }

    @Test
    public void two_named_args() {
        List<NamedArg> args = ResolveNamedArgs.$(ts, "arg1=string, arg2=int64");
        Assert.assertEquals(2, args.size());
        Assert.assertEquals(new NamedArg("arg1", ts.STRING), args.get(0));
        Assert.assertEquals(new NamedArg("arg2", ts.INT64), args.get(1));
    }
}
