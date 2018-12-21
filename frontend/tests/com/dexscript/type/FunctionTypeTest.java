package com.dexscript.type;

import com.dexscript.ast.DexActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        TestAssignable.$(true, hello1, hello2);
        TestAssignable.$(true, hello2, hello1);
        TestAssignable.$(false, hello2, ts.STRING);
    }

    @Test
    public void name_not_assignable() {
        TestAssignable.$(false, func("hello(): string"), func("world(): string"));
    }

    @Test
    public void params_count_not_assignable() {
        TestAssignable.$(false, func("hello(): string"), func("hello(arg0: string): string"));
    }

    @Test
    public void param_name_not_assignable() {
        TestAssignable.$(false, func("hello(a: int64)"), func("hello(b: int64)"));
    }

    @Test
    public void param_type_not_assignable() {
        TestAssignable.$(false, func("hello(arg0: int64)"), func("hello(arg0: string)"));
    }

    @Test
    public void ret_not_assignable() {
        TestAssignable.$(false, func("hello(): string"), func("hello(): void"));
    }

    @Test
    public void param_is_sub_type() {
        FunctionType hello1 = func("hello(arg0: string)");
        FunctionType hello2 = func("hello(arg0: 'example')");
        TestAssignable.$(false, hello1, hello2);
        TestAssignable.$(true, hello2, hello1);
    }

    @Test
    public void ret_is_sub_type() {
        FunctionType hello1 = func("hello(): string");
        FunctionType hello2 = func("hello(): 'example'");
        TestAssignable.$(true, hello1, hello2);
        TestAssignable.$(false, hello2, hello1);
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("Hello(arg0: string): void", func("Hello(arg0: string): void").toString());
    }
}
