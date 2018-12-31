package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class MultiDispatchTest {

    @Test
    public void static_dispatch() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void runtime_dispatch() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
