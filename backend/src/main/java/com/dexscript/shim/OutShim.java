package com.dexscript.shim;

import com.dexscript.ast.*;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Line;
import com.dexscript.pkg.DexPackages;
import com.dexscript.shim.java.*;
import com.dexscript.shim.actor.ActorTable;
import com.dexscript.shim.actor.ActorType;
import com.dexscript.type.*;

import java.lang.reflect.*;
import java.nio.file.Path;
import java.util.*;

public class OutShim {

    public static final String CLASSNAME = "Shim";
    public static final String PACKAGE_NAME = "com.dexscript.transpiled.";
    public static final String QUALIFIED_CLASSNAME = PACKAGE_NAME + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final Map<FunctionEntry, List<FunctionImpl>> entries = new HashMap<>();
    private final Map<FunctionChain, String> chains = new HashMap<>();
    private final Map<String, DexPackage> pkgs = new HashMap<>();
    private final List<GeneratedSubClass> generatedSubClasses = new ArrayList<>();
    private final JavaTypes javaTypes;
    private final ActorTable actorTable;

    public OutShim(TypeSystem ts) {
        this.ts = ts;
        actorTable = new ActorTable(ts);
        javaTypes = new JavaTypes(this);
        g.__("package com.dexscript.transpiled"
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public final class "
        ).__(CLASSNAME
        ).__(" {");
        g.indention("  ");
        g.__(new Line());
    }

    public DexPackage pkg(Path pkgPath) {
        String pkgName = "DS__." + pkgPath.toString().replace('/', '.');
        return pkg(pkgName);
    }

    public DexPackage pkg(Package pkg) {
        return pkg(pkg.getName());
    }

    public DexPackage pkg(String pkgName) {
        DexPackage pkg = pkgs.get(pkgName);
        if (pkg != null) {
            return pkg;
        }
        pkg = new DexPackage(pkgName);
        pkgs.put(pkgName, pkg);
        DexInterface taskInf = new DexInterface(new Text("" +
                "interface Task {\n" +
                "   <T>: interface{}\n" +
                "   Resolve__(value: T)\n" +
                "}"));
        taskInf.attach(pkg);
        ts.defineInterface(taskInf);
        DexInterface promiseInf = new DexInterface(new Text("" +
                "interface Promise {\n" +
                "   <T>: interface{}\n" +
                "   Consume__(): T\n" +
                "}"));
        promiseInf.attach(pkg);
        ts.defineInterface(promiseInf);
        ts.defineBuiltinTypes(pkg);
        return pkg;
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
            if (topLevelDecl.actor() != null) {
                defineActor(topLevelDecl.actor());
            } else if (topLevelDecl.inf() != null) {
                ts.defineInterface(topLevelDecl.inf());
            }
        }
    }

    public void defineInterface(DexInterface inf) {
        ts.defineInterface(inf);
    }

    public void defineActor(DexActor actor) {
        actorTable.define(new ActorType(this, actor));
    }

    public List<ActorType> actors() {
        return actorTable.actors();
    }

    public String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        shims.put(shimName, count);
        return shimName + "__" + count;
    }

    public String dispatch(String funcName, int paramsCount, Dispatched dispatched) {
        FunctionChain chain = new FunctionChain(funcName, paramsCount, dispatched);
        String chainF = chains.get(chain);
        if (chainF != null) {
            return CLASSNAME + "." + chainF;
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

    public void importJavaFunction(Method javaFunction) {
        String funcName = javaFunction.getName();
        List<FunctionParam> dParams = new ArrayList<>();
        for (Parameter jParam : javaFunction.getParameters()) {
            String paramName = jParam.getName();
            DType paramType = javaTypes.resolve(jParam.getType());
            dParams.add(new FunctionParam(paramName, paramType));
        }
        DType ret = javaTypes.resolve(javaFunction.getReturnType());
        FunctionType functionType = new FunctionType(ts, funcName, dParams, ret);
        functionType.implProvider(expandedFunc -> new CallJavaFunction(this, expandedFunc, javaFunction));
    }

    public void importJavaConstructors(Class clazz) {
        for (Constructor ctor : clazz.getConstructors()) {
            importJavaConstructor(ctor);
        }
    }

    public void importJavaConstructor(Constructor ctor) {
        Class clazz = ctor.getDeclaringClass();
        DexSig dexSig = TranslateSig.$(javaTypes, ctor);
        dexSig.attach(this.pkg(clazz.getPackage()));
        FunctionType functionType = new FunctionType(ts, "New__", null, dexSig);
        functionType.implProvider(expandedFunc -> {
            JavaType type = (JavaType) expandedFunc.ret();
            return new NewJavaClass(this, expandedFunc, ctor, type.runtimeClassName());
        });
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
        if (Modifier.isFinal(clazz.getModifiers())) {
            return clazz.getCanonicalName();
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return clazz.getCanonicalName();
        }
        String subClassName = clazz.getSimpleName() + "__" + UUID.randomUUID().toString().substring(0, 8);
        GeneratedSubClass generatedSubClass = new GeneratedSubClass(
                clazz, subClassName);
        generatedSubClasses.add(generatedSubClass);
        return generatedSubClass.qualifiedClassName();
    }
}
