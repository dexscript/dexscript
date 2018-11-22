package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexFileTest {

    @Test
    public void package_clause() {
        DexFile file = new DexFile("package abc\n");
        DexPackageClause pkgClause = file.packageClause();
        Assert.assertEquals("abc", pkgClause.identifier().toString());
    }

    @Test
    public void function_without_package() {
        String src = "" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.rootDecls().size());
        DexRootDecl rootDecl = file.rootDecls().get(0);
        Assert.assertEquals("hello", rootDecl.function().identifier().toString());
    }

    @Test
    public void one_function() {
        String src = "" +
                "package abc;\n" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.rootDecls().size());
        DexRootDecl rootDecl = file.rootDecls().get(0);
        Assert.assertEquals("hello", rootDecl.function().identifier().toString());
    }

    @Test
    public void two_functions() {
        String src = "" +
                "package abc;\n" +
                "function hello() {\n" +
                "}\n" +
                "function world() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(2, file.rootDecls().size());
        Assert.assertEquals("hello", file.rootDecls().get(0).function().identifier().toString());
        Assert.assertEquals("world", file.rootDecls().get(1).function().identifier().toString());
    }
}
