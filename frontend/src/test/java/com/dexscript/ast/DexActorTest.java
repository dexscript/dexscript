package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexActorTest {

    @Test
    public void empty() {
        TestFramework.assertObject(DexActor::$);
    }

    @Test
    public void no_space_between_function_keyword_and_identifier() {
        TestFramework.assertObject(DexActor::$);
    }

    @Test
    public void one_argument() {
        TestFramework.assertObject(DexActor::$);
    }

    @Test
    public void missing_left_paren() {
        TestFramework.assertObject(DexActor::$);
    }

    @Test
    public void missing_function_keyword() {
        TestFramework.assertObject(DexActor::$);
    }

    @Test
    public void missing_left_brace() {
        TestFramework.assertObject(DexActor::$);
    }
}
