package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;
import net.openhft.compiler.CompilerUtils;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public void transpile(String filename, String source) {
        DexFile dexFile = dexFileFactory.createDexFile(filename, source);
        TransOutput out = new TransOutput(filename, source);
        dexFile.accept(new TransFile(out));
        try {
            System.out.println(out.toString());
            Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava("abc.hello", out.toString());
            System.out.println(aClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
