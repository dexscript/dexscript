package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class UnionTest {

    @Test
    public void string_literal_union_string_literal() {
        TypeSystem ts = new TypeSystem();
        StringLiteralType a = new StringLiteralType(ts, "A");
        StringLiteralType b = new StringLiteralType(ts, "B");
        DType ab = a.union(b);
        Assert.assertTrue(ab.isAssignableFrom(a));
        Assert.assertTrue(ab.isAssignableFrom(b));
        Assert.assertFalse(a.isAssignableFrom(ab));
        Assert.assertFalse(b.isAssignableFrom(ab));
    }

    @Test
    public void string_literal_union_string_literal_union_string_literal() {
        TypeSystem ts = new TypeSystem();
        StringLiteralType a = new StringLiteralType(ts, "A");
        StringLiteralType b = new StringLiteralType(ts, "B");
        StringLiteralType c = new StringLiteralType(ts, "C");
        DType abc = a.union(b).union(c);
        Assert.assertTrue(abc.isAssignableFrom(a));
        Assert.assertTrue(abc.isAssignableFrom(b));
        Assert.assertTrue(abc.isAssignableFrom(c));
        Assert.assertFalse(a.isAssignableFrom(abc));
        Assert.assertFalse(b.isAssignableFrom(abc));
        Assert.assertFalse(c.isAssignableFrom(abc));
    }
}
