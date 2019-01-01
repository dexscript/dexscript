package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class CallJavaFunctionTest {

    @Test
    public void normal_function() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaFunctions(clazz);
        }
        TestTranspile.$(oTown);
    }

    @Test
    public void generic_function() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaFunctions(clazz);
        }
        TestTranspile.$(oTown);
    }
}
