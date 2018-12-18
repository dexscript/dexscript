package com.dexscript.transpile.shim;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.transpile.type.JavaTypes;
import com.dexscript.transpile.type.actor.ActorTable;
import com.dexscript.transpile.type.actor.ActorType;
import com.dexscript.transpile.type.java.CallJavaFunction;
import com.dexscript.transpile.type.java.JClassType;
import com.dexscript.type.DType;
import com.dexscript.type.FunctionSig;
import com.dexscript.type.FunctionType;
import com.dexscript.type.TypeSystem;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class OutShim {

    public static final String CLASSNAME = "Shim__";
    public static final String PACKAGE_NAME = "com.dexscript.runtime.gen.";
    public static final String QUALIFIED_CLASSNAME = PACKAGE_NAME + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final Map<FunctionEntry, List<FunctionImpl>> entries = new HashMap<>();
    private final Map<FunctionChain, String> chains = new HashMap<>();
    private final List<GeneratedSubClass> generatedSubClasses = new ArrayList<>();
    private final JavaTypes javaTypes;
    private final ActorTable actorTable;

    public OutShim(TypeSystem ts) {
        this.ts = ts;
        actorTable = new ActorTable(ts);
        ts.defineInterface(new DexInterface("" +
                "interface Task {\n" +
                "   <T>: interface{}\n" +
                "   Resolve__(value: T)\n" +
                "}"));
        ts.defineInterface(new DexInterface("" +
                "interface Promise {\n" +
                "   <T>: interface{}\n" +
                "   Consume__(): T\n" +
                "}"));
        javaTypes = new JavaTypes(this);
        g.__("package com.dexscript.runtime.gen"
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public final class "
        ).__(CLASSNAME
        ).__(" {");
        g.indention("  ");
        g.__(new Line());
    }

    public List<GeneratedSubClass> generatedSubClasses() {
        return generatedSubClasses;
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
        List<DType> params = javaTypes.resolve(javaFunction.getParameterTypes());
        DType ret = javaTypes.resolve(javaFunction.getReturnType());
        FunctionType functionType = new FunctionType(ts, funcName, params, ret);
        ts.defineFunction(functionType);
        CallJavaFunction impl = new CallJavaFunction(this, functionType, javaFunction);
        functionType.setImpl(impl);
    }

    public void importJavaClass(Class clazz) {
        new JClassType(this, clazz);
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

    public String genSubClass(Class clazz) {
        String subClassName = clazz.getSimpleName() + "__" + UUID.randomUUID().toString().substring(0, 8);
        GeneratedSubClass generatedSubClass = new GeneratedSubClass(
                clazz.getCanonicalName(), subClassName);
        generatedSubClasses.add(generatedSubClass);
        return generatedSubClass.qualifiedClassName();
    }
}
