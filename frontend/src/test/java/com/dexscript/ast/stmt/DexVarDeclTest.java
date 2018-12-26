package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexVarDeclTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void no_space_between_var_and_identifier() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_colon() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_blank() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_line_end() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_colon_recover_by_blank() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_colon_recover_by_line_end() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }

    @Test
    public void missing_type() {
        TestFramework.assertParsedAST(DexVarDecl::$);
    }
}
