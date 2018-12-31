package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.Transpile;
import org.junit.Test;

public class GenericFunctionCallTest {

    @Test
    public void call_generic_function_with_type_inference() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void call_generic_function_with_type_arg() {
        TestFramework.assertByList(Transpile::$);
    }
}
