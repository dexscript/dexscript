package com.dexscript.transpile;

import com.dexscript.analyze.CheckError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.resolve.ResolveType;
import com.dexscript.resolve.ResolveValue;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Town {

    public final static boolean DEBUG = true;
    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
    private final Township township;

    public Town() {
        township = new Township();
        township.resolveType = new ResolveType();
        township.resolveValue = new ResolveValue();
    }

    public Town addFile(String fileName, String src) {
        DexFile iFile = new DexFile(new Text(src), fileName);
        if (new CheckError(iFile).hasError()) {
            throw new DexTranspileException();
        }
        for (DexRootDecl iRootDecl : iFile.rootDecls()) {
            if (iRootDecl instanceof DexFunction) {
                OutClass oClass = new OutClass(township, (DexFunction) iRootDecl);
                addSource(oClass.qualifiedClassName(), oClass.toString());
            }
        }
        return this;
    }

    private void addSource(String className, String classSrc) {
        if (DEBUG) {
            System.out.println(">>> " + className);
            System.out.println(classSrc);
        }
        try {
            compiler.addSource(className, classSrc);
        } catch (Exception e) {
            throw new DexTranspileException(e);
        }
    }

    public Map<String, Class<?>> transpile() {
        try {
            return compiler.compileAll();
        } catch (Exception e) {
            throw new DexTranspileException(e);
        }
    }
}
