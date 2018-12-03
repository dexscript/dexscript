package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferFuncArgTest {

    @Test
    public void infer_value() {
        DexFunction func = new DexFunction("" +
                "function Hello(arg: string): string {" +
                "   return arg\n" +
                "}");
        DexReference ref = func.stmts().get(0).asReturn().expr().asRef();
        Value val = InferValue.inferValue(new TypeSystem(), ref);
        Assert.assertEquals("arg: string", val.definedBy().toString());
        Assert.assertEquals(BuiltinTypes.STRING, val.type());
    }
}
