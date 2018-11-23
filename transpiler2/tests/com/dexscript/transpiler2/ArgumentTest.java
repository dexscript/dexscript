package com.dexscript.transpiler2;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentTest {
    @Test
    public void one_argument() {
        String src = "" +
                "function Hello(msg:string): string {\n" +
                "   return msg\n" +
                "}";
        Result res = TranspileOne.__(src, "hello");
        Assert.assertEquals("hello", res.value());
    }
}
