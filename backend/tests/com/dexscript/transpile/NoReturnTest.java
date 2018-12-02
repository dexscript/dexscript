package com.dexscript.transpile;

import org.junit.Test;

public class NoReturnTest {

    @Test
    public void no_arg_no_return() {
        String src = "" +
                "function Hello() {\n" +
                "}";
        TranspileOne.__(src);
    }
}
