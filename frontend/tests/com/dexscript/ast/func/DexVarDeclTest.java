package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexVarDeclTest {

    @Test
    public void matched() {
        DexVarDecl decl = new DexVarDecl("var msg: string");
        Assert.assertEquals("var msg: string", decl.toString());
        Assert.assertEquals("msg", decl.identifier().toString());
        Assert.assertEquals("string", decl.type().toString());
    }

    @Test
    public void no_space_between_var_and_identifier() {
        DexVarDecl decl = new DexVarDecl("varmsg: string");
        Assert.assertEquals("<unmatched>varmsg: string</unmatched>", decl.toString());
    }

    @Test
    public void missing_identifier_recover_by_colon() {
        DexVarDecl decl = new DexVarDecl("var ?:string");
        Assert.assertEquals("var <error/>?:string", decl.toString());
        Assert.assertEquals("<unmatched>?:string</unmatched>", decl.identifier().toString());
        Assert.assertEquals("string", decl.type().toString());
    }

    @Test
    public void missing_identifier_recover_by_blank() {
        DexVarDecl decl = new DexVarDecl("var ? :string");
        Assert.assertEquals("var <error/>? :string", decl.toString());
        Assert.assertEquals("<unmatched>? :string</unmatched>", decl.identifier().toString());
        Assert.assertEquals("string", decl.type().toString());
    }

    @Test
    public void missing_identifier_recover_by_line_end() {
        DexVarDecl decl = new DexVarDecl("var ?;:string");
        Assert.assertEquals("var <error/>?", decl.toString());
    }

    @Test
    public void missing_colon_recover_by_blank() {
        DexVarDecl decl = new DexVarDecl("var msg( string");
        Assert.assertEquals("var msg<error/>( string", decl.toString());
        Assert.assertEquals("msg", decl.identifier().toString());
        Assert.assertEquals("string", decl.type().toString());
    }

    @Test
    public void missing_colon_recover_by_line_end() {
        DexVarDecl decl = new DexVarDecl("var msg(; string");
        Assert.assertEquals("var msg<error/>(", decl.toString());
    }

    @Test
    public void missing_type() {
        DexVarDecl decl = new DexVarDecl("var msg: ??");
        Assert.assertEquals("var msg:<error/> ??", decl.toString());
        Assert.assertEquals("msg", decl.identifier().toString());
        Assert.assertEquals("<unmatched> ??</unmatched>", decl.type().toString());
    }
}
