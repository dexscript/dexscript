package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoolTypeTest {

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
        Assert.assertEquals("bool", ts.BOOL.toString());
        Assert.assertEquals("true", ts.literalOf(true).toString());
        Assert.assertEquals("false", ts.literalOf(false).toString());
        Assert.assertEquals("(const)true", ts.constOf(true).toString());
        Assert.assertEquals("(const)false", ts.constOf(false).toString());
    }

}
