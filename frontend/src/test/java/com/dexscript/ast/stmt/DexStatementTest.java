package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexStatementTest {

    @Test
    public void expression() {
        TestFramework.assertObject(DexStatement::$parse);
    }

    @Test
    public void short_var_decl() {
        TestFramework.assertObject(DexStatement::$parse);
    }

    @Test
    public void block() {
        TestFramework.assertObject(DexStatement::$parse);
    }

    @Test
    public void stmt_in_block() {
        TestFramework.assertObject(DexStatement::$parse);
    }
}
