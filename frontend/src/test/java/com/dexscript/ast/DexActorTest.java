package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexActorTest {

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexActor::$);
    }

    @Test
    public void no_space_between_function_keyword_and_identifier() {
        TestFramework.assertParsedAST(DexActor::$);
    }

    @Test
    public void one_argument() {
        TestFramework.assertParsedAST(DexActor::$);
    }

    @Test
    public void missing_left_paren() {
        TestFramework.assertParsedAST(DexActor::$);
    }

    @Test
    public void missing_function_keyword() {
        TestFramework.assertParsedAST(DexActor::$);
    }

    @Test
    public void missing_left_brace() {
        TestFramework.assertParsedAST(DexActor::$);
    }
}
