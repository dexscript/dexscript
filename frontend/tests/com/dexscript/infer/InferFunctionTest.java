package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InferFunctionTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void argument_can_be_referenced() {
        DexActor func = DexActor.$("" +
                "function Hello(arg: string): string {\n" +
                "   return arg\n" +
                "}");
        DexValueRef ref = func.stmts().get(0).asReturn().expr().asRef();
        Value val = InferValue.$(ts, ref);
        Assert.assertEquals("arg: string", val.definedBy().toString());
        Assert.assertEquals(ts.STRING, val.type());
    }

    @Test
    public void generic_argument_can_be_referenced() {
        DexActor func = DexActor.$("" +
                "function Hello(<T>: string, arg: T): T {\n" +
                "   return arg\n" +
                "}");
        DexValueRef ref = func.stmts().get(0).asReturn().expr().asRef();
        Value val = InferValue.$(ts, ref);
        Assert.assertEquals("arg: T", val.definedBy().toString());
        Assert.assertEquals(ts.STRING, val.type());
    }
}
