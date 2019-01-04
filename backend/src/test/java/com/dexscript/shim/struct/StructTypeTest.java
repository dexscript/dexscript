package com.dexscript.shim.struct;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexStructExpr;
import com.dexscript.infer.InferType;
import com.dexscript.shim.TestAssignable;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.TypeSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class StructTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void one_field() {
        List<String> codes = testDataFromMySection().codes();
        InterfaceType inf = ts.defineInterface(DexInterface.$(codes.get(0)));
        StructType struct = new StructType(ts, DexStructExpr.$(codes.get(1)));
        TestAssignable.$(true, inf, struct);
    }
}
