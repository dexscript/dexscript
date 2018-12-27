package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexProduceStmtTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void produced_is_optional() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void no_space_after_produce_keyword() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void missing_produced_recover_by_blank() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void missing_produced_recover_by_line_end() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void missing_right_arrow_recover_by_blank() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void missing_right_arrow_recover_by_line_end() {
        TestFramework.assertObject(DexProduceStmt::$);
    }

    @Test
    public void missing_target() {
        TestFramework.assertObject(DexProduceStmt::$);
    }
}
