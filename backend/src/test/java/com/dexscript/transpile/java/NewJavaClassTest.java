package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class NewJavaClassTest {

    @Test
    public void new_java_class() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaConstructors(clazz);
        }
        TestTranspile.$(oTown);
    }
}
