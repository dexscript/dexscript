package com.dexscript.transpile;

import com.dexscript.runtime.UInt8;
import org.junit.Assert;
import org.junit.Test;

public class TranspileNewArrayTest {

    @Test
    public void new_array() {
        Object result = Transpile.$("" +
                "function Hello(): uint8[] {\n" +
                "   bytes := new uint8[3]\n" +
                "   return bytes\n" +
                "}");
        Assert.assertEquals(3, ((UInt8[]) result).length);
    }
}
