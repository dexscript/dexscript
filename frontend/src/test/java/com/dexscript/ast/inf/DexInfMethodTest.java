package com.dexscript.ast.inf;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInfMethodTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexInfMethod::$);
    }

    @Test
    public void invalid_identifier() {
        TestFramework.assertObject(DexInfMethod::$);
    }

    @Test
    public void invalid_signature() {
        TestFramework.assertObject(DexInfMethod::$);
    }
}
