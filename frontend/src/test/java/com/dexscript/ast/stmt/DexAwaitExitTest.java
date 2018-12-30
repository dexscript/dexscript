package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitExitTest {
    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexAwaitExit::$);
    }

    @Test
    public void unmatched() {
        TestFramework.assertParsedAST(DexAwaitExit::$);
    }
}
