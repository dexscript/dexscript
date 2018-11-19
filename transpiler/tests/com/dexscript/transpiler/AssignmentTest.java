package com.dexscript.transpiler;

import org.junit.Test;

public class AssignmentTest extends TranspilerTest {

    @Test
    public void test_short_var_decl() {
        String src = "" +
                "function Hello() {\n" +
                "   val := 'hello'\n" +
                "}\n";
        transpile0(src);
    }

    @Test
    public void test_var_decl() {
        String src = "" +
                "function Hello() {\n" +
                "   var val: string\n" +
                "   val = 'hello'" +
                "}\n";
        transpile0(src);
    }
}
