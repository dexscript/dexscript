package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileArrayTest {

    @Test
    public void encode_string_to_byte_array() {
        Byte[] result = (Byte[]) Transpile.$("" +
                "function Hello(): byte[] {\n" +
                "   return 'hello'.encode()\n" +
                "}");
        Assert.assertEquals(5, result.length);
    }
}
