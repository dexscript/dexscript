package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntegerTypeTest {

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
        Assert.assertEquals("int64", ts.INT64.toString());
        Assert.assertEquals("int32", ts.INT32.toString());
        Assert.assertEquals("100", ts.literalOf(100).toString());
        Assert.assertEquals("(const)100", ts.constOf(100).toString());
    }
}
