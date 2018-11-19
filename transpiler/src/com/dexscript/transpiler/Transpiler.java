package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;
import com.dexscript.runtime.AddBoats;
import com.dexscript.runtime.CastBoats;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public Map<String, Class<?>> transpile(String filename, String source) {
        DexFile iFile = dexFileFactory.createDexFile(filename, source);
        OutShim oShim = new OutShim(null);
        AddBoats.init(oShim);
        CastBoats.init(oShim);
        OutFile oFile = new OutFile(iFile, oShim);
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        for (OutClass oClass : oFile.oClasses()) {
            addToCompiler(compiler, oClass.qualifiedClassName(), oClass.toString());
        }
        addToCompiler(compiler, OutShim.SHIM_PACKAGE + "." + OutShim.SHIM_CLASS, oShim.generate());
        try {
            return compiler.compileAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addToCompiler(InMemoryJavaCompiler compiler, String className, String src) {
        try {
            System.out.println(className);
            String lines[] = src.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                System.out.println((i+1) + ":\t" + line);
            }
            compiler.addSource(className, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
