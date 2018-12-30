package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexStatementTest {

    @Test
    public void expression() {
        TestFramework.assertParsedAST(DexStatement::$parse);
    }

    @Test
    public void short_var_decl() {
        TestFramework.assertParsedAST(DexStatement::$parse);
    }

    @Test
    public void block() {
        TestFramework.assertParsedAST(DexStatement::$parse);
    }

    @Test
    public void stmt_in_block() {
        TestFramework.assertParsedAST(DexStatement::$parse);
    }
}
