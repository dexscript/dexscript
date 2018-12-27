package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIfStmtTest {
    @Test
    public void no_else() {
        TestFramework.assertObject(DexIfStmt::$);
    }

    @Test
    public void with_else_if_only() {
        TestFramework.assertObject(DexIfStmt::$);
    }

    @Test
    public void with_else_only() {
        TestFramework.assertObject(DexIfStmt::$);
    }

    @Test
    public void with_else_if_and_else() {
        TestFramework.assertObject(DexIfStmt::$);
    }

    @Test
    public void else_before_else_if() {
        TestFramework.assertObject(DexIfStmt::$);
    }
}
