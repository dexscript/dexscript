package com.dexscript.shim.java;

import com.dexscript.shim.TestAssignable;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.java.DefineJavaClass;
import com.dexscript.type.core.DType;
import org.junit.Assert;
import org.junit.Test;

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
        TestAssignable.$(oTown);
    }

    private static void testJavaTypeAssignable() {
        OutTown oTown = new OutTown();
        Class<?> jType = DefineJavaClass.$(oTown).get("some.java.pkg.SomeClass");
        oTown.oShim().javaTypes().resolve(jType);
        TestAssignable.$(oTown);
    }
}
