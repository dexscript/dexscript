package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class UnionTest {

    @Test
    public void string_literal_union_string_literal() {
        StringLiteralType a = new StringLiteralType("A");
        StringLiteralType b = new StringLiteralType("B");
        Assert.assertTrue(a.union(b).isAssignableFrom(a));
        Assert.assertTrue(a.union(b).isAssignableFrom(b));
    }

    @Test
    public void string_literal_union_string_literal_union_string_literal() {
        StringLiteralType a = new StringLiteralType("A");
        StringLiteralType b = new StringLiteralType("B");
        StringLiteralType c = new StringLiteralType("C");
        Type abc = a.union(b).union(c);
        Assert.assertTrue(abc.isAssignableFrom(a));
        Assert.assertTrue(abc.isAssignableFrom(b));
        Assert.assertTrue(abc.isAssignableFrom(c));
    }
}
