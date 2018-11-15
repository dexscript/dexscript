package com.dexscript.transpiler;

import org.junit.Test;

public class AssignmentTest extends TranspilerTest {

    @Test
    public void testAssignment() {
        String src = "" +
                "function Hello() {\n" +
                "   val := 'hello'\n" +
                "}\n";
        transpile0(src);
    }
}
