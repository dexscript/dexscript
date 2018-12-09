package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.*;
import com.dexscript.transpile.shim.impl.*;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.transpile.type.CheckType;
import com.dexscript.type.ActorType;
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
    public static final String QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen__." + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final List<Impl> impls = new ArrayList<>();
    private final Map<VirtualFunction, List<Impl>> virtualFunctions = new HashMap<>();

    public OutShim(TypeSystem ts) {
        this.ts = ts;
        g.__("package com.dexscript.runtime.gen__"
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
        CheckType checkType = new CheckType(null, null);
        for (Impl impl : impls) {
            impl.finish(g, checkType);
        }
        for (Map.Entry<VirtualFunction, List<Impl>> entry : virtualFunctions.entrySet()) {
            entry.getKey().finish(g, entry.getValue());
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

    public void defineActor(DexFunction function) {
        ts.defineActor(function, new ActorType.ImplProvider() {
            @Override
            public void callFunc(FunctionType functionType, DexFunction func) {
                String newF = CLASSNAME + "." + allocateShim("new__" + function.actorName());
                String canF = CLASSNAME + "." + allocateShim("can__" + function.actorName());
                CallActor impl = new CallActor(functionType, function, canF, newF);
                impls.add(impl);
                VirtualFunction virtualFunction = new VirtualFunction(function.functionName(), function.params().size());
                virtualFunctions.computeIfAbsent(virtualFunction, k -> new ArrayList<>()).add(impl);
            }

            @Override
            public void newFunc(FunctionType functionType, DexFunction func) {
                String newF = CLASSNAME + "." + allocateShim("new__" + function.actorName());
                String canF = CLASSNAME + "." + allocateShim("can__" + function.actorName());
                impls.add(new NewActor(functionType, function, canF, newF));
            }

            @Override
            public void innerCallFunc(FunctionType functionType, DexFunction func, DexAwaitConsumer awaitConsumer) {
                String funcName = awaitConsumer.identifier().toString();
                String newF = CLASSNAME + "." + allocateShim("new__" + funcName);
                String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
                String outerClassName = OutTopLevelClass.qualifiedClassNameOf(func);
                CallInnerActor innerActor = new CallInnerActor(functionType, outerClassName, awaitConsumer, canF, newF);
                impls.add(innerActor);
            }

            @Override
            public void innerNewFunc(FunctionType functionType, DexFunction func, DexAwaitConsumer awaitConsumer) {
                String funcName = awaitConsumer.identifier().toString();
                String newF = CLASSNAME + "." + allocateShim("new__" + funcName);
                String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
                String outerClassName = OutTopLevelClass.qualifiedClassNameOf(func);
                NewInnerActor innerActor = new NewInnerActor(functionType, outerClassName, awaitConsumer, canF, newF);
                impls.add(innerActor);
            }
        });
    }

    private String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        shims.put(shimName, count);
        return shimName + "__" + count;
    }

    public String combineNewF(String funcName, int paramsCount, List<FunctionType> funcTypes) {
        String cNewF = allocateShim("cnew__" + funcName);
        g.__("public static Promise "
        ).__(cNewF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : funcTypes) {
                if (!(funcType.attachment() instanceof Impl)) {
                    throw new IllegalStateException("no implementation attached to function: " + funcType);
                }
                Impl implEntry = (Impl) funcType.attachment();
                g.__("if ("
                ).__(implEntry.canF());
                InvokeParams.$(g, paramsCount, false);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    if (implEntry.newF() == null) {
                        g.__("return "
                        ).__(implEntry.callF());
                        InvokeParams.$(g, paramsCount, false);
                        g.__(new Line(";"));
                    } else {
                        g.__("return "
                        ).__(implEntry.newF());
                        InvokeParams.$(g, paramsCount, true);
                        g.__(new Line(";"));
                    }
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
        return CLASSNAME + "." + cNewF;
    }

    public void importJavaClass(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                importJavaFunction(method);
            }
        }
    }

    private void importJavaFunction(Method javaFunction) {
        String funcName = javaFunction.getName();
        String callF = CLASSNAME + "." + allocateShim("call__" + funcName);
        String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
        List<Type> params = ts.resolveType(javaFunction.getParameterTypes());
        Type ret = ts.resolveType(javaFunction.getReturnType());
        FunctionType functionType = new FunctionType(funcName, params, ret);
        ts.defineFunction(functionType);
        CallJavaFunction impl = new CallJavaFunction(functionType, javaFunction, canF, callF);
        impls.add(impl);
    }

    public static String stripPrefix(String f) {
        if (!f.startsWith(OutShim.CLASSNAME)) {
            throw new IllegalArgumentException();
        }
        return f.substring(OutShim.CLASSNAME.length() + 1);
    }
}
