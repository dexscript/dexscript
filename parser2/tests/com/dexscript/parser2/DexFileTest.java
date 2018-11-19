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

}
