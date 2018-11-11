package com.dexscript.parser;

import com.intellij.psi.PsiFile;
import org.junit.Test;

public class RawParserTest {

    @Test
    public void testError() {
        PsiFile elem = DexFileFactory.createFile("test.dex", "package abc");
        System.out.println(elem.getFirstChild());
    }
}
