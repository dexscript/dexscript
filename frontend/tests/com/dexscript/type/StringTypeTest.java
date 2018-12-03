package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class StringTypeTest {

    @Test
    public void string_is_assignable_from_string() {
        Assert.assertTrue(new StringType().isAssignableFrom(BuiltinTypes.STRING));
        Assert.assertTrue(BuiltinTypes.STRING.isAssignableFrom(new StringType()));
    }

    @Test
    public void string_is_assignable_from_string_literal() {
        Assert.assertTrue(BuiltinTypes.STRING.isAssignableFrom(new StringLiteralType("hello")));
        Assert.assertFalse(new StringLiteralType("hello").isAssignableFrom(BuiltinTypes.STRING));
    }
}
