package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexBlockTest {

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void one_statement() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void statements_separated_by_new_line() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void statements_separated_by_semicolon() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void walk_up() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void recover_from_invalid_statement_by_line_end() {
        TestFramework.assertParsedAST(DexBlock::$);
    }

    @Test
    public void recover_from_last_invalid_statement() {
        TestFramework.assertParsedAST(DexBlock::$);
    }
}
