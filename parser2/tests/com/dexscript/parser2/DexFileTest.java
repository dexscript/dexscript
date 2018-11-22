package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexFileTest {

    @Test
    public void package_clause() {
        DexFile file = new DexFile("package abc\n");
        DexPackageClause pkgClause = file.packageClause();
        Assert.assertEquals("package", pkgClause.packageKeyword().toString());
        Assert.assertEquals("abc", pkgClause.identifier().toString());
    }

    @Test
    public void functions() {
        String src = "" +
                "function hello() {\n" +
                "}";
        DexFile file = new DexFile(src);
        Assert.assertEquals(1, file.rootDecls().size());
        DexRootDecl rootDecl = file.rootDecls().get(0);
        Assert.assertEquals("hello", rootDecl.function().identifier().toString());
    }
}
