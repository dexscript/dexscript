package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class CallJavaMethodTest {

    @Test
    public void call_java_method() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaConstructors(clazz);
        }
        TestTranspile.$(oTown);
    }
}
