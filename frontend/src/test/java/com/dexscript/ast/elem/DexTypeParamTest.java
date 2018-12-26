package com.dexscript.ast.elem;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexTypeParamTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexTypeParam::$);
    }

    @Test
    public void missing_identifier() {
        TestFramework.assertParsedAST(DexTypeParam::$);
    }

    @Test
    public void missing_right_angle_bracket() {
        TestFramework.assertParsedAST(DexTypeParam::$);
    }

    @Test
    public void missing_colon() {
        TestFramework.assertParsedAST(DexTypeParam::$);
    }

    @Test
    public void missing_type() {
        TestFramework.assertParsedAST(DexTypeParam::$);
    }
}
