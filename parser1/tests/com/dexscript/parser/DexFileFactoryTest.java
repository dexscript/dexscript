package com.dexscript.parser;

import com.intellij.psi.PsiFile;
import org.junit.Assert;
import org.junit.Test;

public class DexFileFactoryTest {

    @Test
    public void testCreate() {
        try (DexFileFactory dexFileFactory = new DexFileFactory()) {
            PsiFile elem = dexFileFactory.createDexFile("test.ds", "package abc");
            Assert.assertEquals("PACKAGE_CLAUSE", elem.getFirstChild().toString());
        }
    }
}
