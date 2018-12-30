package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexStringConstTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexStringConst::$);
    }

    @Test
    public void escape_single_quote() {
        TestFramework.assertParsedAST(DexStringConst::$);
    }

    @Test
    public void backslash_without_following_char() {
        TestFramework.assertParsedAST(DexStringConst::$);
    }

    @Test
    public void missing_right_quote() {
        TestFramework.assertParsedAST(DexStringConst::$);
    }

    @Test
    public void without_left_quote() {
        TestFramework.assertParsedAST(DexStringConst::$);
    }
}
