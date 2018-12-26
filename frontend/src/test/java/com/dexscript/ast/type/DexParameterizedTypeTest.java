package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexParameterizedTypeTest {

    @Test
    public void one_arg() {
        TestFramework.assertParsedAST(DexType::$parse);
    }

    @Test
    public void two_args() {
        TestFramework.assertParsedAST(DexType::$parse);
    }

    @Test
    public void nested_expansion() {
        TestFramework.assertParsedAST(DexType::$parse);
    }

    @Test
    public void missing_type_arg_recover_by_comma() {
        TestFramework.assertParsedAST(DexType::$parse);
    }

    @Test
    public void missing_type_arg_recover_by_right_angle_bracket() {
        TestFramework.assertParsedAST(DexType::$parse);
    }

    @Test
    public void missing_comma_recover_by_blank() {
        TestFramework.assertParsedAST(DexType::$parse);
    }
}
