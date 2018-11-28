package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class SingleDispatchTest {

    @Test
    public void single_dispatch() {
        String src = "" +
                "function Hello(): string {" +
                "   return Ident('hello')\n" +
                "}\n" +
                "function Ident(msg: string): string {\n" +
                "   return msg\n" +
                "}\n" +
                "function Ident(val: int64): int64 {\n" +
                "   return val\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("hello", result.value());
    }
}
