package com.dexscript.transpile;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class MethodCallTest {

    @Test
    public void method_call_is_same_as_function_call() {
        TestFramework.assertByList(Transpile::$);
    }
}
