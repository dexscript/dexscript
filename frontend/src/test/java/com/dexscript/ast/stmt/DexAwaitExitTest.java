package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitExitTest {
    @Test
    public void matched() {
        TestFramework.assertObject(DexAwaitExit::$);
    }

    @Test
    public void unmatched() {
        TestFramework.assertObject(DexAwaitExit::$);
    }
}
