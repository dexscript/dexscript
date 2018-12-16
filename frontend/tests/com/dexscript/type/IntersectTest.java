package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class IntersectTest {

    @Test
    public void string_literals_intersect_string_literals() {
        TypeSystem ts = new TypeSystem();
        StringLiteralType a = new StringLiteralType(ts, "A");
        StringLiteralType b = new StringLiteralType(ts, "B");
        StringLiteralType c = new StringLiteralType(ts, "C");
        DType ab = a.union(b);
        DType bc = b.union(c);
        DType intersected = ab.intersect(bc);
        Assert.assertFalse(intersected.isAssignableFrom(a));
        Assert.assertTrue(intersected.isAssignableFrom(b));
        Assert.assertFalse(intersected.isAssignableFrom(c));
        Assert.assertFalse(a.isAssignableFrom(intersected));
        Assert.assertTrue(b.isAssignableFrom(intersected));
        Assert.assertFalse(c.isAssignableFrom(intersected));
    }
}
