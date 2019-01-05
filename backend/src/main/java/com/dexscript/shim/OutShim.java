package com.dexscript.shim;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Line;
import com.dexscript.runtime.std.ArithmeticLib;
import com.dexscript.runtime.std.ComparisonLib;
import com.dexscript.runtime.std.IOLib;
import com.dexscript.shim.actor.ActorTable;
import com.dexscript.shim.actor.ActorType;
import com.dexscript.shim.java.*;
import com.dexscript.type.core.*;

import java.lang.reflect.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        defineNewArray();
        importJavaFunctions(ArithmeticLib.class);
        importJavaFunctions(ComparisonLib.class);
        importJavaFunctions(IOLib.class);
    }

    private void defineNewArray() {
        new ArrayType(this);
        DexSig dexSig = new DexSig(new Text("(<T>: interface{}, class: 'Array', length: int32): Array<T>"));
        dexSig.attach(DexPackage.BUILTIN);
        FunctionType functionType = new FunctionType(ts, "New__", null, dexSig);
        functionType.implProvider(expandedFunc -> new NewJavaArray(this, expandedFunc));
    }

    public DexPackage pkg(Path pkgPath) {
        String pkgName = "DEXSCRIPT_ROOT" + pkgPath.toString().replace('/', '.');
        return pkg(pkgName);
    }

    public DexPackage pkg(Type jType) {
        String typeName = jType.getTypeName();
        if (jType instanceof Class) {
            typeName = ((Class) jType).getCanonicalName();
        }
        int dotPos = typeName.lastIndexOf('.');
        if (dotPos == -1) {
            return pkg("JAVA_ROOT");
        }
        String packageName = typeName.substring(0, dotPos);
        return pkg(packageName);
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
        DexInterface arrayInf = new DexInterface(new Text("" +
                "interface Array {\n" +
                "   <T>: interface{}\n" +
                "   get(index: int32): T\n" +
                "   set(index: int32, element: T)\n" +
                "}"));
        arrayInf.attach(pkg);
        ts.defineInterface(arrayInf);
        ts.defineBuiltinTypes(pkg);
        return pkg;
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
        DexSig dexSig = TranslateJavaCtor.$(javaTypes, ctor);
        dexSig.attach(this.pkg(clazz));
        FunctionType functionType = new FunctionType(ts, "New__", null, dexSig);
        functionType.implProvider(expandedFunc -> new NewJavaClass(this, expandedFunc, ctor));
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
