package com.dexscript.ast.inf;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInfTypeParamTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_identifier_recover_by_right_angle_bracket() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_identifier_recover_by_blank() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_identifier_recover_by_line_end() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_right_angle_bracket_recover_by_colon() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_right_angle_bracket_recover_by_blank() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_colon_recover_by_blank() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }

    @Test
    public void missing_type() {
        TestFramework.assertObject(DexInfTypeParam::$);
    }
}
