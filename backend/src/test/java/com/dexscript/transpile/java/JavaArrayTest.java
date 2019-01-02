package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Assert;
import org.junit.Test;

public class JavaArrayTest {

    @Test
    public void get_set_array_element() {
        OutTown oTown = new OutTown();
        for (Class<?> clazz : DefineJavaClass.$(oTown).values()) {
            oTown.oShim().importJavaFunctions(clazz);
        }
        TestTranspile.$(oTown);
    }

    @Test
    public void new_array_of_interface() {
        OutTown oTown = new OutTown();
        TestTranspile.$(oTown);
    }
}
