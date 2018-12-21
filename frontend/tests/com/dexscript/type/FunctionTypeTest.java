package com.dexscript.type;

import com.dexscript.ast.DexActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    private FunctionType func(String src) {
        DexActor actor = new DexActor("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        return new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
    }

    @Test
    public void assignable() {
        FunctionType hello1 = func("hello(): string");
        FunctionType hello2 = func("hello(): string");
        Assert.assertTrue(new IsAssignable(hello1, hello2).result());
        Assert.assertTrue(new IsAssignable(hello2, hello1).result());
        Assert.assertFalse(new IsAssignable(hello2, ts.STRING).result());
    }

    @Test
    public void name_not_assignable() {
        IsAssignable isAssignable = new IsAssignable(
                func("hello(): string"),
                func("world(): string"));
        Assert.assertFalse(isAssignable.result());
    }

    @Test
    public void params_count_not_assignable() {
        IsAssignable isAssignable = new IsAssignable(
                func("hello(): string"),
                func("hello(arg0: string): string"));
        Assert.assertFalse(isAssignable.result());
    }

    @Test
    public void params_not_assignable() {
        IsAssignable isAssignable = new IsAssignable(
                func("hello(arg0: int64)"),
                func("hello(arg0: string)"));
        Assert.assertFalse(isAssignable.result());
    }

    @Test
    public void ret_not_assignable() {
        IsAssignable isAssignable = new IsAssignable(
                func("hello(): string"),
                func("hello(): void"));
        Assert.assertFalse(isAssignable.result());
    }

    @Test
    public void param_is_sub_type() {
        FunctionType hello1 = new FunctionType(ts, "hello", new ArrayList<FunctionParam>() {{
            add(new FunctionParam("arg0", ts.STRING));
        }}, ts.VOID);
        FunctionType hello2 = new FunctionType(ts, "hello", new ArrayList<FunctionParam>() {{
            add(new FunctionParam("arg0", ts.literalOf("example")));
        }}, ts.VOID);
        Assert.assertFalse(IsAssignable.$(hello1, hello2));
        Assert.assertTrue(IsAssignable.$(hello2, hello1));
    }

    @Test
    public void ret_is_sub_type() {
        FunctionType hello1 = new FunctionType(ts, "hello",
                new ArrayList<>(), ts.STRING);
        FunctionType hello2 = new FunctionType(ts, "hello",
                new ArrayList<>(), new StringLiteralType(ts, "example"));
        Assert.assertTrue(IsAssignable.$(hello1, hello2));
        Assert.assertFalse(IsAssignable.$(hello2, hello1));
    }

    @Test
    public void test_to_string() {
        FunctionType func = new FunctionType(ts, "Hello", new ArrayList<FunctionParam>(){{
            add(new FunctionParam("arg0", ts.STRING));
        }}, ts.VOID);
        Assert.assertEquals("Hello(arg0: string): void", func.toString());
    }
}
