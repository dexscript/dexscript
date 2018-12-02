package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class InterfaceArgTest {

    @Test
    public void call_via_interface() {
        String src = "" +
                "function Hello(duck: Duck): string {" +
                "   return duck.Quack()\n" +
                "}\n" +
                "interface Duck {\n" +
                "   ::Quack(duck: Duck): string\n" +
                "}\n" +
                "function Quack(duck: string): string {\n" +
                "   return duck + ' quack'\n" +
                "}";
        Result result = TranspileOne.__(src, (Object)"donald");
        Assert.assertEquals("donald quack", result.value());
    }
}
