package com.dexscript.transpiler;

import org.junit.Test;

public class TranspilerTest {

    @Test
    public void test() {
        Transpiler transpiler = new Transpiler();
        transpiler.transpile("hello.dex", "" +
                "package abc\n" +
                "function hello(): string {\n" +
                "   return 'hello'\n" +
                "}\n");
    }
}
