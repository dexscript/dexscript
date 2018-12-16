package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InferShortVarDeclTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void local_variable_can_be_referenced() {
        DexActor func = new DexActor("" +
                "function Hello(arg: string): string {\n" +
                "   local := arg\n" +
                "   return local\n" +
                "}");
        DexValueRef ref = func.stmts().get(1).asReturn().expr().asRef();
        Value value = InferValue.$(ts, ref);
        Assert.assertEquals("local", value.definedBy().toString());
        Assert.assertEquals(ts.STRING, value.type());
    }

    @Test
    public void local_variable_does_not_leak_out_block() {
        DexActor func = new DexActor("" +
                "function Hello(arg: string): string {\n" +
                "   {\n" +
                "       local := arg\n" +
                "   }\n" +
                "   return local\n" +
                "}");
        DexValueRef ref = func.stmts().get(1).asReturn().expr().asRef();
        Value value = InferValue.$(ts, ref);
        Assert.assertNull(value.definedBy());
        Assert.assertEquals(ts.UNDEFINED, value.type());
    }
}
