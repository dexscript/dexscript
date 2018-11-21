package com.dexscript.parser2.stmt;

import com.dexscript.parser2.stmt.DexShortVarDecl;
import org.junit.Assert;
import org.junit.Test;

public class DexShortVarDeclTest {

    @Test
    public void matched() {
        Assert.assertEquals("a:=b", new DexShortVarDecl("a:=b").toString());
        Assert.assertEquals("a,b:=c", new DexShortVarDecl("a,b:=c").toString());
    }

    @Test
    public void invalid_identifier() {
        Assert.assertEquals("<unmatched>?:=b</unmatched>", new DexShortVarDecl("?:=b").toString());
        Assert.assertEquals("<unmatched>a,?:=b</unmatched>", new DexShortVarDecl("a,?:=b").toString());
    }

    @Test
    public void missing_comma() {
        Assert.assertEquals("<unmatched>a(b:=c</unmatched>", new DexShortVarDecl("a(b:=c").toString());
    }

    @Test
    public void missing_colon() {
        Assert.assertEquals("<unmatched>a,b(=c</unmatched>", new DexShortVarDecl("a,b(=c").toString());
    }

    @Test
    public void expr_with_error() {
        Assert.assertEquals("a:=", new DexShortVarDecl("a:=").toString());
        Assert.assertEquals("<error/>", new DexShortVarDecl("a:=").expr().toString());
        Assert.assertEquals("a:=b(", new DexShortVarDecl("a:=b(").toString());
        Assert.assertEquals("b(<error/>", new DexShortVarDecl("a:=b(").expr().toString());
    }
}
