package com.dexscript.denotation;

import org.junit.Assert;
import org.junit.Test;

public class TypeVoidTest {

    @Test
    public void assignable_to_void() {
        Assert.assertTrue(new TypeVoid().isAssignableFrom(new TypeVoid()));
    }
}
