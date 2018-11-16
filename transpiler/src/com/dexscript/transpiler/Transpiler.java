package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public Map<String, Class<?>> transpile(String filename, String source) {
        DexFile iFile = dexFileFactory.createDexFile(filename, source);
        OutFile oFile = new OutFile(iFile);
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        for (OutClass oClass : oFile.oClasses()) {
            oClass.addToCompiler(compiler);
        }
        oFile.genShim(compiler);
        try {
            return compiler.compileAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
