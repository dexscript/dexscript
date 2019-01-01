package com.dexscript.shim.java;

import com.dexscript.type.DType;
import com.dexscript.type.IsAssignable;
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
