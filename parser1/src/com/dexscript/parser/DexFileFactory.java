package com.dexscript.parser;

import com.dexscript.psi.DexFile;
import com.intellij.testFramework.ParsingTestCase;

public class DexFileFactory extends ParsingTestCase implements AutoCloseable {

    public DexFileFactory() {
        super("parser", "ds", new DexParserDefinition());
        try {
            setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DexFile createDexFile(String filename, String source) {
        return (DexFile) createPsiFile(filename, source);
    }

    @Override
    public void close() {
        try {
            tearDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
