package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class CallJavaMethodTest {

    @Test
    public void normal_method() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaConstructors(clazz);
        }
        TestTranspile.$(oTown);
    }

    @Test
    public void generic_method_referenced_class_type_param() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaConstructors(clazz);
        }
        TestTranspile.$(oTown);
    }
}
