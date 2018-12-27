package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexVarDeclTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void no_space_between_var_and_identifier() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_colon() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_blank() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_identifier_recover_by_line_end() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_colon_recover_by_blank() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_colon_recover_by_line_end() {
        TestFramework.assertObject(DexVarDecl::$);
    }

    @Test
    public void missing_type() {
        TestFramework.assertObject(DexVarDecl::$);
    }
}
