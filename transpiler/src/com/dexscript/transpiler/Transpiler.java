package com.dexscript.transpiler;

import com.dexscript.parser.DexFileFactory;
import com.dexscript.psi.DexFile;
import com.dexscript.runtime.Result1;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Transpiler implements AutoCloseable {

    private DexFileFactory dexFileFactory = new DexFileFactory();

    public void transpile(String filename, String source) {
        DexFile iFile = dexFileFactory.createDexFile(filename, source);
        OutFile oFile = new OutFile(iFile);
        iFile.accept(oFile);
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        for (OutClass oClass : oFile.oClasses()) {
            oClass.addToCompiler(compiler);
        }
        oFile.genShim(compiler);
        try {
            Map<String, Class<?>> classes = compiler.compileAll();
            Object obj = classes.get("abc.Hello").newInstance();
            System.out.println(((Result1) obj).result1__());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        dexFileFactory.close();
    }
}
