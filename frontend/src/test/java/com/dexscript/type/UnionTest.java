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
        Assert.assertTrue(IsAssignable.$(ab, a));
        Assert.assertTrue(IsAssignable.$(ab, b));
        Assert.assertFalse(IsAssignable.$(a, ab));
        Assert.assertFalse(IsAssignable.$(b, ab));
    }

    @Test
    public void string_literal_union_string_literal_union_string_literal() {
        TypeSystem ts = new TypeSystem();
        StringLiteralType a = new StringLiteralType(ts, "A");
        StringLiteralType b = new StringLiteralType(ts, "B");
        StringLiteralType c = new StringLiteralType(ts, "C");
        DType abc = a.union(b).union(c);
        Assert.assertTrue(IsAssignable.$(abc, a));
        Assert.assertTrue(IsAssignable.$(abc, b));
        Assert.assertTrue(IsAssignable.$(abc, c));
        Assert.assertFalse(IsAssignable.$(a, abc));
        Assert.assertFalse(IsAssignable.$(b, abc));
        Assert.assertFalse(IsAssignable.$(c, abc));
    }
}
