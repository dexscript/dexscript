package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

import java.util.ArrayList;

public class JavaContainerTest {

    @Test
    public void use_array_list() {
        OutTown oTown = new OutTown();
        oTown.oShim().importJavaConstructors(ArrayList.class);
        TestTranspile.$(oTown);
    }
}
