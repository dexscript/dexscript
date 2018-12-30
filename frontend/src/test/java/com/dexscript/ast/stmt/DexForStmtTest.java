package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexForStmtTest {

    @Test
    public void forever() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void for_condition() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void for_with_init_and_post() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void init_is_optional() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void post_is_optional() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void init_and_post_is_optional() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }

    @Test
    public void for_with_3_semi_colon() {
        TestFramework.assertParsedAST(DexForStmt::$);
    }
}
