package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitStmtTest {

    @Test
    public void empty() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }

    @Test
    public void await_consumer() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }

    @Test
    public void await_producer() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }

    @Test
    public void await_exit() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }

    @Test
    public void await_multiple_cases() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }

    @Test
    public void recover_from_invalid_statement() {
        TestFramework.assertObject(DexAwaitStmt::$);
    }
}
