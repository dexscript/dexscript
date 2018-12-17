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
    public void string_is_assignable_from_string() {
        Assert.assertTrue(new IsAssignable(new StringType(ts), ts.STRING).result());
        Assert.assertTrue(new IsAssignable(ts.STRING, new StringType(ts)).result());
    }

    @Test
    public void string_is_assignable_from_string_literal() {
        Assert.assertTrue(new IsAssignable(ts.STRING, new StringLiteralType(ts, "hello")).result());
        Assert.assertFalse(new IsAssignable(new StringLiteralType(ts, "hello"), ts.STRING).result());
    }

    @Test
    public void string_literal_is_assignable_from_string_literal() {
        Assert.assertTrue(new IsAssignable(
                new StringLiteralType(ts, "A"),
                new StringLiteralType(ts, "A")).result());
    }
}
