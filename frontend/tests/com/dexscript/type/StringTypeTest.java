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
        Assert.assertTrue(new StringType(ts).isAssignableFrom(ts.STRING));
        Assert.assertTrue(ts.STRING.isAssignableFrom(new StringType(ts)));
    }

    @Test
    public void string_is_assignable_from_string_literal() {
        Assert.assertTrue(ts.STRING.isAssignableFrom(new StringLiteralType(ts, "hello")));
        Assert.assertFalse(new StringLiteralType(ts, "hello").isAssignableFrom(ts.STRING));
    }

    @Test
    public void string_literal_is_assignable_from_string_literal() {
        Assert.assertTrue(new StringLiteralType(ts, "A").isAssignableFrom(
                new StringLiteralType(ts, "A")));
    }
}
