package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class MethodCallTest {

    @Test
    public void method_call_is_same_as_function_call() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
