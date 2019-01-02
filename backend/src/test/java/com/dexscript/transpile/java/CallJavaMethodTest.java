package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class CallJavaMethodTest {

    @Test
    public void normal_method() {
        testJavaMethod();
    }

    @Test
    public void generic_method_referenced_class_type_param() {
        testJavaMethod();
    }

    @Test
    public void generic_method_referenced_method_type_param() {
        testJavaMethod();
    }

    private static void testJavaMethod() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaConstructors(clazz);
        }
        TestTranspile.$(oTown);
    }
}
