package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexRootDeclTest {

    @Test
    public void missing_left_paren() {
        String src = "" +
                "function hello ) {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<unmatched>function hello ) {\n" +
                "}\n" +
                "</unmatched>", new DexRootDecl(src).toString());
    }

    @Test
    public void skip_garbage_in_prelude() {
        String src = "" +
                " abc function hello () {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<error/>function hello () {\n" +
                "}\n", new DexRootDecl(src).toString());
    }
}
