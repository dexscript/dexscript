package com.dexscript.transpile;

import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.runtime.std.BasicOperators;
import com.dexscript.transpile.shim.GeneratedSubClass;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.*;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutTown {

    public interface OnSourceAdded {
        void handle(String className, String classSrc);
    }

    public static OnSourceAdded ON_SOURCE_ADDED = (className, classSrc) -> {
    };

    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler
            .newInstance()
            .ignoreWarnings();
    private final List<DexFile> iFiles = new ArrayList<>();
    private final TypeSystem ts = new TypeSystem();
    private final OutShim oShim = new OutShim(ts);

    public OutTown() {
        oShim.importJavaFunctions(BasicOperators.class);
        oShim.importJavaConstructors(File.class);
        oShim.importJavaConstructors(ArrayList.class);
    }

    public OutTown addFile(String fileName, String src) {
        DexFile iFile = new DexFile(new Text(src), fileName);
        if (new CheckSyntaxError(iFile).hasError()) {
            throw new DexRuntimeException();
        }
        oShim.defineFile(iFile);
        iFiles.add(iFile);
        return this;
    }

    public Map<String, Class<?>> transpile() {
        for (DexFile iFile : iFiles) {
            if (new CheckSemanticError(ts, iFile).hasError()) {
                throw new DexRuntimeException();
            }
            for (DexTopLevelDecl iTopLevelDecl : iFile.topLevelDecls()) {
                if (iTopLevelDecl.function() != null) {
                    DexActor function = iTopLevelDecl.function();
                    ensureTypeLoaded(function);
                    OutTopLevelClass oClass = new OutTopLevelClass(ts, oShim, function);
                    addSource(oClass.qualifiedClassName(), oClass.toString());
                }
            }
        }
        try {
            addSource(OutShim.QUALIFIED_CLASSNAME, oShim.finish());
            for (GeneratedSubClass generatedSubClass : oShim.generatedSubClasses()) {
                addSource(generatedSubClass.qualifiedClassName(), generatedSubClass.gen());
            }
            return compiler.compileAll();
        } catch (Exception e) {
            throw new DexRuntimeException(e);
        }
    }

    private void ensureTypeLoaded(DexActor function) {
        String funcName = function.functionName();
        TypeTable localTypeTable = new TypeTable(ts, function.typeParams());
        List<DType> args = new ArrayList<>();
        for (DexParam param : function.params()) {
            DType arg = ResolveType.$(ts, localTypeTable, param.paramType());
            args.add(arg);
        }
        Invocation ivc = new Invocation(funcName, null, args, null);
        List<FunctionSig.Invoked> invokeds = ts.invoke(ivc);
        for (FunctionSig.Invoked invoked : invokeds) {
            invoked.function().impl();
        }
    }

    private void addSource(String className, String classSrc) {
        try {
            compiler.addSource(className, classSrc);
            ON_SOURCE_ADDED.handle(className, classSrc);
        } catch (Exception e) {
            throw new DexRuntimeException(e);
        }
    }
}
