package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexTopLevelDeclTest {

    @Test
    public void missing_left_paren() {
        String src = "" +
                "function hello ) {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<unmatched>function hello ) {\n" +
                "}\n" +
                "</unmatched>", new DexTopLevelDecl(src).toString());
    }

    @Test
    public void skip_garbage_in_prelude() {
        String src = "" +
                " example function hello () {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<error/>function hello () {\n" +
                "}\n", new DexTopLevelDecl(src).toString());
    }
}
