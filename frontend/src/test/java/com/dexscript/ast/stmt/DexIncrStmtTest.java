package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIncrStmtTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexIncrStmt::$);
    }
}
