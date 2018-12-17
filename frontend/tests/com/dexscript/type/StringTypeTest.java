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
        Assert.assertTrue(IsAssignable.$(new StringType(ts), ts.STRING));
        Assert.assertTrue(IsAssignable.$(ts.STRING, new StringType(ts)));
    }

    @Test
    public void string_is_assignable_from_string_literal() {
        Assert.assertTrue(IsAssignable.$(ts.STRING, new StringLiteralType(ts, "hello")));
        Assert.assertFalse(IsAssignable.$(new StringLiteralType(ts, "hello"), ts.STRING));
    }

    @Test
    public void string_literal_is_assignable_from_string_literal() {
        Assert.assertTrue(IsAssignable.$(
                new StringLiteralType(ts, "A"),
                new StringLiteralType(ts, "A")));
    }
}
