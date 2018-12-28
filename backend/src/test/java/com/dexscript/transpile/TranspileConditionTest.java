package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileConditionTest {

    @Test
    public void equal() {
        Object result = Transpile.$("function Hello(): bool { return 1 == 1 }");
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void less_than() {
        Object result = Transpile.$("function Hello(): bool { return 1 < 2 }");
        Assert.assertTrue((Boolean) result);
    }
}
