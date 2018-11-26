package com.dexscript.transpile;

import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.core.Text;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transpiler {

    public final static boolean DEBUG = true;
    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
    private final List<DexFile> iFiles = new ArrayList<>();
    private final Town town;

    public Transpiler() {
        town = new Town();
    }

    public Transpiler addFile(String fileName, String src) {
        DexFile iFile = new DexFile(new Text(src), fileName);
        if (new CheckSyntaxError(iFile).hasError()) {
            throw new DexTranspileException();
        }
        town.define(iFile);
        iFiles.add(iFile);
        return this;
    }

    private void addSource(String className, String classSrc) {
        if (DEBUG) {
            System.out.println(">>> " + className);
            String lines[] = classSrc.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                System.out.println((i + 1) + ":\t" + line);
            }
        }
        try {
            compiler.addSource(className, classSrc);
        } catch (Exception e) {
            throw new DexTranspileException(e);
        }
    }

    public Map<String, Class<?>> transpile() {
        for (DexFile iFile : iFiles) {
            town.checkSemanticError(iFile);
            for (DexRootDecl iRootDecl : iFile.rootDecls()) {
                if (iRootDecl instanceof DexFunction) {
                    OutClass oClass = new OutClass(town, (DexFunction) iRootDecl);
                    addSource(oClass.qualifiedClassName(), oClass.toString());
                }
            }
        }
        try {
            addSource(Town.TOWN_QUALIFIED_CLASSNAME, town.finish());
            return compiler.compileAll();
        } catch (Exception e) {
            throw new DexTranspileException(e);
        }
    }
}
