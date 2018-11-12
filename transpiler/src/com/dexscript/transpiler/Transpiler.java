package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public void transpile(String filename, String source) {
        DexFile dexFile = dexFileFactory.createDexFile(filename, source);
        TransOutput out = new TransOutput(filename, source);
        dexFile.accept(new TranFile(out));
        System.out.println(out.toString());
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
