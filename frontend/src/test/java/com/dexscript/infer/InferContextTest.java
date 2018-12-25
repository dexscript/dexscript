package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InferContextTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void ref_context() {
        InterfaceType contextType = ts.defineInterface(DexInterface.$("" +
                "interface $ {" +
                "}"));
        DexActor func = DexActor.$("" +
                "function Hello(): interface{} {\n" +
                "   return $\n" +
                "}");
        func.attach(DexPackage.DUMMY);
        DexValueRef ref = func.stmts().get(0).asReturn().expr().asRef();
        Value value = InferValue.$(ts, ref);
        Assert.assertEquals(contextType, value.type());
    }
}
