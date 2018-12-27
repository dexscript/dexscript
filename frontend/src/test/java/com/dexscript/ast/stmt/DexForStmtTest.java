package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexForStmtTest {

    @Test
    public void forever() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void for_condition() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void for_with_init_and_post() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void init_is_optional() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void post_is_optional() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void init_and_post_is_optional() {
        TestFramework.assertObject(DexForStmt::$);
    }

    @Test
    public void for_with_3_semi_colon() {
        TestFramework.assertObject(DexForStmt::$);
    }
}
