package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitStmtTest {

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }

    @Test
    public void await_consumer() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }

    @Test
    public void await_producer() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }

    @Test
    public void await_exit() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }

    @Test
    public void await_multiple_cases() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }

    @Test
    public void recover_from_invalid_statement() {
        TestFramework.assertParsedAST(DexAwaitStmt::$);
    }
}
