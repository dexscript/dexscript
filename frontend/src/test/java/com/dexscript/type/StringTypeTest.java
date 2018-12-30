package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringTypeTest {

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
        Assert.assertEquals("string", ts.STRING.toString());
        Assert.assertEquals("'hello'", ts.literalOf("hello").toString());
        Assert.assertEquals("(const)'hello'", ts.constOf("hello").toString());
    }
}
