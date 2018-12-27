package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexReturnStmtTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexReturnStmt::$);
    }

    @Test
    public void garbage_in_prelude() {
        TestFramework.assertObject(DexReturnStmt::$);
    }

    @Test
    public void missing_expr_recover_by_file_end() {
        TestFramework.assertObject(DexReturnStmt::$);
    }

    @Test
    public void missing_expr_recover_by_line_end() {
        TestFramework.assertObject(DexReturnStmt::$);
    }

    @Test
    public void return_without_space() {
        TestFramework.assertObject(DexReturnStmt::$);
    }

    @Test
    public void walk_up() {
        TestFramework.assertObject(DexReturnStmt::$);
    }
}
