package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FloatTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void assignable_relationship() {
        TestAssignable.$();
    }

    @Test
    public void equals_relationship() {
        TestEquals.$();
    }

    @Test
    public void to_string() {
        Assert.assertEquals("float64", ts.FLOAT64.toString());
        Assert.assertEquals("float32", ts.FLOAT32.toString());
        Assert.assertEquals("(const)100.0", ts.constOf(100.0).toString());
    }
}
