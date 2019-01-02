package com.dexscript.shim.java;

import com.dexscript.ast.DexInterface;
import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.java.DefineJavaClass;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class JavaTypeTest {

    @Test
    public void just_object() {
        testJavaTypeAssignable();
    }

    @Test
    public void one_param() {
        testJavaTypeAssignable();
    }

    @Test
    public void one_class_type_param() {
        testJavaTypeAssignable();
    }

    @Test
    public void argument_referenced_class_type_param() {
        testJavaTypeAssignable();
    }

    @Test
    public void one_method_type_param() {
        testJavaTypeAssignable();
    }

    @Test
    public void return_parameterized_self() {
        testJavaTypeAssignable();
    }

    @Test
    public void one_dimension_array() {
        OutTown oTown = new OutTown();
        DType dType = oTown.oShim().javaTypes().resolve(String[].class);
        Assert.assertEquals("String_array", dType.toString());
        testJavaTypeAssignable(oTown);
    }

    private static void testJavaTypeAssignable() {
        OutTown oTown = new OutTown();
        Class<?> jType = DefineJavaClass.$(oTown).get("some.java.pkg.SomeClass");
        oTown.oShim().javaTypes().resolve(jType);
        testJavaTypeAssignable(oTown);
    }

    private static void testJavaTypeAssignable(OutTown oTown) {
        FluentAPI testData = testDataFromMySection();
        TypeSystem ts = oTown.oShim().typeSystem();
        for (String code : testData.codes("dexscript")) {
            ts.defineInterface(DexInterface.$(code));
        }
        for (Row row : testData.table().body) {
            DType to = ResolvePosArgs.$(ts, stripQuote(row.get(1))).get(0);
            DType from = ResolvePosArgs.$(ts, stripQuote(row.get(2))).get(0);
            TestAssignable.$("true".equals(row.get(0)), to, from);
        }
    }
}
