package com.dexscript.parser;

import com.dexscript.psi.DexFile;
import com.intellij.testFramework.ParsingTestCase;

public class DexFileFactory extends ParsingTestCase implements AutoCloseable {

    public DexFileFactory() throws Exception {
        super("parser", "dex", new DexParserDefinition());
        setUp();
    }

    public DexFile createDexFile(String name, String text) {
        return (DexFile) createPsiFile(name, text);
    }

    @Override
    public void close() throws Exception {
        tearDown();
    }
}
