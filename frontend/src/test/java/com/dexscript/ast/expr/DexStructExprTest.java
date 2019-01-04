package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexStructExprTest {

    @Test
    public void one_field() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void two_fields() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void zero_field_is_invalid() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void field_name_should_not_be_quoted() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void missing_field_value() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void missing_field_name() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void missing_colon() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }

    @Test
    public void missing_right_brace() {
        TestFramework.assertParsedAST(DexStructExpr::$);
    }
}
