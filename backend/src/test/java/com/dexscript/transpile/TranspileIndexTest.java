package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileIndexTest {

    @Test
    public void index_is_get() {
        String result = (String) TestTranspile.$("" +
                "function Hello(): string {\n" +
                "   obj := new World()\n" +
                "   return obj[0]\n" +
                "}\n" +
                "function World() {\n" +
                "   await {\n" +
                "   case get(index: int64): string {\n" +
                "       resolve 'hello' -> get\n" +
                "   }}\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
