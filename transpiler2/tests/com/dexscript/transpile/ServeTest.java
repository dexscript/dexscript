package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ServeTest {

    @Test
    public void serve() {
        String src = "" +
                "function Hello(): string {\n" +
                "   a := A{}\n" +
                "   return a.AA()\n" +
                "}\n" +
                "function A() {\n" +
                "   await {\n" +
                "   case AA(): string {\n" +
                "       'hello' -> AA\n" +
                "   }}\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("hello", result.value());
    }
}
