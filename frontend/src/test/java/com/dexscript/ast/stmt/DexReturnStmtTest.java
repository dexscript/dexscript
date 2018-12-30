package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexReturnStmtTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }

    @Test
    public void garbage_in_prelude() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }

    @Test
    public void missing_expr_recover_by_file_end() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }

    @Test
    public void missing_expr_recover_by_line_end() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }

    @Test
    public void return_without_space() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }

    @Test
    public void walk_up() {
        TestFramework.assertParsedAST(DexReturnStmt::$);
    }
}
