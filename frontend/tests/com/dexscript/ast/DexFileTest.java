package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexFileTest {

    @Test
    public void package_clause() {
        DexFile file = new DexFile("package example\n");
        DexPackageClause pkgClause = file.packageClause();
        Assert.assertEquals("example", pkgClause.identifier().toString());
    }

    @Test
    public void function_without_package() {
        String src = "" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.topLevelDecls().size());
        DexActor rootDecl = file.topLevelDecls().get(0).function();
        Assert.assertEquals("hello", rootDecl.identifier().toString());
    }

    @Test
    public void one_function() {
        String src = "" +
                "package example;\n" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.topLevelDecls().size());
        DexActor rootDecl = file.topLevelDecls().get(0).function();
        Assert.assertEquals("hello", rootDecl.identifier().toString());
    }

    @Test
    public void two_functions() {
        String src = "" +
                "package example;\n" +
                "function hello() {\n" +
                "}\n" +
                "function world() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(2, file.topLevelDecls().size());
        DexActor function0 = file.topLevelDecls().get(0).function();
        DexActor function1 = file.topLevelDecls().get(1).function();
        Assert.assertEquals("hello", function0.identifier().toString());
        Assert.assertEquals("world", function1.identifier().toString());
    }
}
