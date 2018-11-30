package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ActorArgTest {

    @Test
    public void pass_actor_as_argument() {
        String src = "" +
                "function Hello(): string {" +
                "   a := A{}\n" +
                "   return B(a)\n" +
                "}\n" +
                "function A(): string {\n" +
                "   return 'hello'\n" +
                "}\n" +
                "function B(a: A): string {" +
                "   return <-a\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("hello", result.value());
    }
}
