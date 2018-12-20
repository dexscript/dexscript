package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexStringConstTest {

    @Test
    public void matched() {
        Assert.assertEquals("'hello'", new DexStringConst("'hello'").toString());
    }

    @Test
    public void escape_single_quote() {
        Assert.assertEquals("'hello\\'world'", new DexStringConst("'hello\\'world'").toString());
    }

    @Test
    public void backslash_without_following_char() {
        Assert.assertEquals("'hello\\<error/>", new DexStringConst("'hello\\").toString());
    }

    @Test
    public void missing_right_quote() {
        Assert.assertEquals("'hello<error/>", new DexStringConst("'hello").toString());
    }

    @Test
    public void without_left_quote() {
        Assert.assertEquals("<unmatched>hello'</unmatched>", new DexStringConst("hello'").toString());
    }
}
