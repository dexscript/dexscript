package com.dexscript.transpile;

import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.runtime.condition.Add__Int64__Int64;
import com.dexscript.runtime.condition.Equal__Int64__Int64;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.FunctionType;
import com.dexscript.type.TypeSystem;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutTown {

    public interface OnSourceAdded {
        void handle(String className, String classSrc);
    }

    public static OnSourceAdded ON_SOURCE_ADDED = (className, classSrc) -> {
    };

    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
    private final List<DexFile> iFiles = new ArrayList<>();
    private final TypeSystem ts = new TypeSystem();
    private final OutShim oShim = new OutShim(ts);

    public OutTown() {
        ts.defineFunction(Equal__Int64__Int64.FUNCTION_TYPE);
        ts.defineFunction(Add__Int64__Int64.FUNCTION_TYPE);
    }

    public OutTown addFile(String fileName, String src) {
        DexFile iFile = new DexFile(new Text(src), fileName);
        if (new CheckSyntaxError(iFile).hasError()) {
            throw new DexRuntimeException();
        }
        ts.defineFile(iFile);
        oShim.defineFile(iFile);
        iFiles.add(iFile);
        return this;
    }

    public Map<String, Class<?>> transpile() {
        for (DexFile iFile : iFiles) {
            for (DexTopLevelDecl iTopLevelDecl : iFile.topLevelDecls()) {
                if (iTopLevelDecl.function() != null) {
                    OutTopLevelClass oClass = new OutTopLevelClass(ts, oShim, iTopLevelDecl.function());
                    addSource(oClass.qualifiedClassName(), oClass.toString());
                }
            }
        }
        try {
            addSource(OutShim.QUALIFIED_CLASSNAME, oShim.finish());
            return compiler.compileAll();
        } catch (Exception e) {
            throw new DexRuntimeException(e);
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
