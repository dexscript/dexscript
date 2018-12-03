package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class IntersectTest {

    @Test
    public void string_literals_intersect_string_literals() {
        StringLiteralType a = new StringLiteralType("A");
        StringLiteralType b = new StringLiteralType("B");
        StringLiteralType c = new StringLiteralType("C");
        Type ab = a.union(b);
        Type bc = b.union(c);
        Type intersected = ab.intersect(bc);
        Assert.assertFalse(intersected.isAssignableFrom(a));
        Assert.assertTrue(intersected.isAssignableFrom(b));
        Assert.assertFalse(intersected.isAssignableFrom(c));
        Assert.assertFalse(a.isAssignableFrom(intersected));
        Assert.assertTrue(b.isAssignableFrom(intersected));
        Assert.assertFalse(c.isAssignableFrom(intersected));
    }
}
