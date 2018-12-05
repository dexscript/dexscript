package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferFunctionTest {

    @Test
    public void argument_can_be_referenced() {
        DexFunction func = new DexFunction("" +
                "function Hello(arg: string): string {\n" +
                "   return arg\n" +
                "}");
        DexValueRef ref = func.stmts().get(0).asReturn().expr().asRef();
        Value val = InferValue.inferValue(new TypeSystem(), ref);
        Assert.assertEquals("arg: string", val.definedBy().toString());
        Assert.assertEquals(BuiltinTypes.STRING, val.type());
    }
}
