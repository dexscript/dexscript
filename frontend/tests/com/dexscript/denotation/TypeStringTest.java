package com.dexscript.denotation;

import org.junit.Assert;
import org.junit.Test;

public class TypeStringTest {

    @Test
    public void string_is_assignable_from_string() {
        Assert.assertTrue(new TypeString().isAssignableFrom(BuiltinTypes.STRING));
        Assert.assertTrue(BuiltinTypes.STRING.isAssignableFrom(new TypeString()));
    }

    @Test
    public void string_is_assignable_from_string_literal() {
        Assert.assertTrue(BuiltinTypes.STRING.isAssignableFrom(new TypeStringLiteral("hello")));
    }
}
