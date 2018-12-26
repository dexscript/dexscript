package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInterfaceTest {

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void global_spi() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void no_space_between_interface_keyword_and_identifier() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void no_space_between_identifier_and_left_brace() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void inf_method() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void inf_function() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void recover_invalid_inf_member_by_line_end() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void type_parameter() {
        TestFramework.assertParsedAST(DexInterface::$);
    }

    @Test
    public void two_type_parameters() {
        TestFramework.assertParsedAST(DexInterface::$);
    }
}
