package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class GenericFunctionCallTest {

    @Test
    public void call_generic_function_with_type_inference() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void call_generic_function_with_type_arg() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
