package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAssignStmtTest {

    @Test
    public void one_target() {
        TestFramework.assertObject(DexAssignStmt::$);
    }

    @Test
    public void two_targets() {
        TestFramework.assertObject(DexAssignStmt::$);
    }

    @Test
    public void missing_expr() {
        TestFramework.assertObject(DexAssignStmt::$);
    }
}
