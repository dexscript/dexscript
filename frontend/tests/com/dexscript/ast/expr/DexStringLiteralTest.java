package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexStringLiteralTest {

    @Test
    public void matched() {
        Assert.assertEquals("'hello'", new DexStringLiteral("'hello'").toString());
    }

    @Test
    public void escape_single_quote() {
        Assert.assertEquals("'hello\\'world'", new DexStringLiteral("'hello\\'world'").toString());
    }

    @Test
    public void backslash_without_following_char() {
        Assert.assertEquals("'hello\\<error/>", new DexStringLiteral("'hello\\").toString());
    }

    @Test
    public void missing_right_quote() {
        Assert.assertEquals("'hello<error/>", new DexStringLiteral("'hello").toString());
    }

    @Test
    public void without_left_quote() {
        Assert.assertEquals("<unmatched>hello'</unmatched>", new DexStringLiteral("hello'").toString());
    }
}
