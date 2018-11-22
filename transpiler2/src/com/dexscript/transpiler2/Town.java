package com.dexscript.transpiler2;

import com.dexscript.parser2.DexFile;
import com.dexscript.parser2.DexRootDecl;
import com.dexscript.parser2.core.Text;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.Map;

public class Town {

    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();

    public Town addFile(String fileName, String src) {
        DexFile iFile = new DexFile(new Text(src), fileName);
        for (DexRootDecl iRootDecl : iFile.rootDecls()) {
        }
        return this;
    }

    public Map<String, Class> transpile() {
        return null;
    }
}
