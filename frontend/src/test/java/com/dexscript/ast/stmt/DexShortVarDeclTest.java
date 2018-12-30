package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexShortVarDeclTest {

    @Test
    public void one_target() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void two_targets() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void invalid_identifier() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void second_decl_invalid() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void missing_comma() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void missing_colon() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }

    @Test
    public void expr_with_error() {
        TestFramework.assertParsedAST(DexShortVarDecl::$);
    }
}
