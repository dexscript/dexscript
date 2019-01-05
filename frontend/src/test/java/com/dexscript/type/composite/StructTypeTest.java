package com.dexscript.type.composite;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexStructExpr;
import com.dexscript.type.core.TestAssignable;
import com.dexscript.type.core.TypeSystem;
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
        InterfaceType inf = new InterfaceType(ts, DexInterface.$(codes.get(0)));
        StructType struct = new StructType(ts, DexStructExpr.$(codes.get(1)));
        TestAssignable.$(true, inf, struct);
    }

    @Test
    public void two_fields() {
        List<String> codes = testDataFromMySection().codes();
        InterfaceType inf = new InterfaceType(ts, DexInterface.$(codes.get(0)));
        StructType struct = new StructType(ts, DexStructExpr.$(codes.get(1)));
        TestAssignable.$(true, inf, struct);
    }
}
