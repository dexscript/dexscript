package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileShortVarDecl {

    @Test
    public void reference() {
        Object result = Transpile.$("" +
                "function Hello(): string {" +
                "   msg := 'hello'\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
