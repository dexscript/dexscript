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

    public static class Class2 {
        public static String[] newArray() {
            return new String[]{"hello"};
        }
    }

    @Test
    public void get_element_from_array() {
        OutTown oTown = new OutTown();
        oTown.oShim().importJavaFunctions(Class2.class);
        String ret = (String) TestTranspile.$(oTown, "", "" +
                "function Hello(): string {\n" +
                "   arr := newArray()\n" +
                "   return arr[0]\n" +
                "}");
        Assert.assertEquals("hello", ret);
    }
}
