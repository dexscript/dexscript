package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;
import com.dexscript.runtime.Result1;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public void transpile(String filename, String source) {
        DexFile dexFile = dexFileFactory.createDexFile(filename, source);
        TransFile transFile = new TransFile(filename, source);
        dexFile.accept(transFile);
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        for (TranspiledClass transpiledClass : transFile.getTranspiledClasses()) {
            try {
                System.out.println(transpiledClass.toString());
                transpiledClass.addToCompiler(compiler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        transFile.generateShim(compiler);
        try {
            Map<String, Class<?>> classes = compiler.compileAll();
            Object obj = classes.get("abc.hello").newInstance();
            System.out.println(((Result1)obj).result1__());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
