package com.dexscript.type;

import org.junit.Assert;

public interface TestAssignable {

    static void $(boolean expected, DType to, DType from) {
        IsAssignable isAssignable = new IsAssignable(to, from);
        if (expected != isAssignable.result()) {
            isAssignable.dump();
            Assert.fail();
        }
    }
}
