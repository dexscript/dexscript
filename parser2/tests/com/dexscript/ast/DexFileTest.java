package com.dexscript.ast;

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
        DexFunction rootDecl = file.rootDecls().get(0).function();
        Assert.assertEquals("hello", rootDecl.identifier().toString());
    }

    @Test
    public void one_function() {
        String src = "" +
                "package abc;\n" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.rootDecls().size());
        DexFunction rootDecl = file.rootDecls().get(0).function();
        Assert.assertEquals("hello", rootDecl.identifier().toString());
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
        DexFunction function0 = file.rootDecls().get(0).function();
        DexFunction function1 = file.rootDecls().get(1).function();
        Assert.assertEquals("hello", function0.identifier().toString());
        Assert.assertEquals("world", function1.identifier().toString());
    }
}
