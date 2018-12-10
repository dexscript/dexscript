package com.dexscript.transpile;

import com.dexscript.runtime.UInt8;
import org.junit.Assert;
import org.junit.Test;

public class TranspileArrayTest {

    @Test
    public void encode_string_to_byte_array() {
        Object[] result = (Object[]) Transpile.$("" +
                "function Hello(): uint8[] {\n" +
                "   return 'hello'.Encode()\n" +
                "}");
        Assert.assertEquals(5, result.length);
        Assert.assertEquals(new UInt8((byte) 'h'), result[0]);
    }
}
