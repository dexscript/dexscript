package com.dexscript.transpile.call;

import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class MultiDispatchTest {

    @Test
    public void static_dispatch() {
        TestTranspile.$();
    }

    @Test
    public void runtime_dispatch() {
        TestTranspile.$();
    }
}
