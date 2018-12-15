package com.dexscript.transpile.shim;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.transpile.type.TypeCandidate;
import com.dexscript.transpile.type.JavaTypes;
import com.dexscript.transpile.type.actor.ActorTable;
import com.dexscript.transpile.type.actor.ActorType;
import com.dexscript.transpile.type.actor.PromiseType;
import com.dexscript.transpile.type.actor.TaskType;
import com.dexscript.transpile.type.java.CallJavaFunction;
import com.dexscript.transpile.type.java.JavaClassType;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutShim {

    public static final String CLASSNAME = "Shim__";
    public static final String QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen." + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final Map<FunctionEntry, List<FunctionImpl>> entries = new HashMap<>();
    private final Map<FunctionChain, String> chains = new HashMap<>();
    private final JavaTypes javaTypes = new JavaTypes(this);
    private final ActorTable actorTable;

    public OutShim(TypeSystem ts) {
        this.ts = ts;
        actorTable = new ActorTable(ts);
        /*
        interface Task {
            <T>: interface{}
            Resolve__(value: T)
        }
         */
        new TaskType(ts);
        /*
        interface Promise {
            <T>: interface{}
            Consume__(): T
        }
         */
        new PromiseType(ts);
        g.__("package com.dexscript.runtime.gen"
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public final class "
        ).__(CLASSNAME
        ).__(" {");
        g.indention("  ");
        g.__(new Line());
    }

    public String finish() {
        if (finished) {
            throw new IllegalStateException();
        }
        finished = true;
        for (Map.Entry<FunctionEntry, List<FunctionImpl>> entry : entries.entrySet()) {
            entry.getKey().gen(g, entry.getValue(), javaTypes);
        }
        for (Map.Entry<FunctionChain, String> entry : chains.entrySet()) {
            entry.getKey().gen(g, entry.getValue(), javaTypes);
        }
        g.indention("");
        g.__(new Line());
        g.__(new Line("}")); // end of class Shim__
        return g.toString();
    }

    public void defineFile(DexFile file) {
        for (DexTopLevelDecl topLevelDecl : file.topLevelDecls()) {
            if (topLevelDecl.function() != null) {
                defineActor(topLevelDecl.function());
            } else if (topLevelDecl.inf() != null) {
                ts.defineInterface(topLevelDecl.inf());
            }
        }
    }

    public void defineActor(DexActor actor) {
        actorTable.define(new ActorType(this, actor));
    }

    public String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        shims.put(shimName, count);
        return shimName + "__" + count;
    }

    public String dispatch(String funcName, int paramsCount, List<FunctionSig.Invoked> invokeds) {
        FunctionChain chain = new FunctionChain(funcName, paramsCount, invokeds);
        String chainF = chains.get(chain);
        if (chainF != null) {
            return chainF;
        }
        chainF = allocateShim("chain__" + funcName);
        chains.put(chain, chainF);
        return CLASSNAME + "." + chainF;
    }

    public void importJavaFunctions(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                importJavaFunction(method);
            }
        }
    }

    private void importJavaFunction(Method javaFunction) {
        String funcName = javaFunction.getName();
        List<Type> params = javaTypes.resolve(javaFunction.getParameterTypes());
        Type ret = javaTypes.resolve(javaFunction.getReturnType());
        FunctionType functionType = new FunctionType(funcName, params, ret);
        ts.defineFunction(functionType);
        CallJavaFunction impl = new CallJavaFunction(this, functionType, javaFunction);
        functionType.attach(impl);
    }

    public void importJavaClass(Class clazz) {
        new JavaClassType(this, clazz);
    }

    public Gen g() {
        return g;
    }

    public TypeSystem typeSystem() {
        return ts;
    }

    public void registerImpl(FunctionImpl impl) {
        FunctionType functionType = impl.functionType();
        FunctionEntry entry = new FunctionEntry(functionType.name(), functionType.params().size());
        entries.computeIfAbsent(entry, k -> new ArrayList<>()).add(impl);
    }

    public JavaTypes javaTypes() {
        return javaTypes;
    }
}
