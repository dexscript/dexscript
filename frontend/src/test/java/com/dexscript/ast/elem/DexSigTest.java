package com.dexscript.ast.elem;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexSigTest {

    @Test
    public void one_param() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void two_params() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void return_value() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void return_void() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void one_type_param() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void two_type_params() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void invalid_param_name_recover_by_comma() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void invalid_param_name_recover_by_right_paren() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void invalid_param_name_recover_by_line_end() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void invalid_param_name_recover_by_file_end() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void param_name_missing_colon() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void param_name_missing_type() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        TestFramework.assertParsedAST(DexSig::$);
    }

    @Test
    public void missing_return_type() {
        TestFramework.assertParsedAST(DexSig::$);
    }
}
