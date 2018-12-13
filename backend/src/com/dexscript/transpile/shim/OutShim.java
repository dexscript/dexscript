package com.dexscript.transpile.shim;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.*;
import com.dexscript.transpile.shim.impl.*;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.transpile.type.*;
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
    private final List<FunctionImpl> impls = new ArrayList<>();
    private final Map<FunctionEntry, List<FunctionImpl>> entries = new HashMap<>();
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
        CheckType checkType = new CheckType(null, null);
        for (FunctionImpl impl : impls) {
            impl.finish(g, checkType);
        }
        for (Map.Entry<FunctionEntry, List<FunctionImpl>> entry : entries.entrySet()) {
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

    public void defineActor(DexActor actor) {
        ActorType actorType = new ActorType(ts, actor, new ActorType.ImplProvider() {
            @Override
            public Object callFunc(FunctionType functionType, DexActor func) {
                String newF = CLASSNAME + "." + allocateShim("new__" + actor.actorName());
                String canF = CLASSNAME + "." + allocateShim("can__" + actor.actorName());
                boolean hasAwait = new HasAwait(ts, func).result();
                CallActor impl = new CallActor(functionType, actor, canF, newF, hasAwait);
                impls.add(impl);
                FunctionEntry functionEntry = new FunctionEntry(actor.functionName(), actor.params().size());
                entries.computeIfAbsent(functionEntry, k -> new ArrayList<>()).add(impl);
                return impl;
            }

            @Override
            public Object newFunc(FunctionType functionType, DexActor func) {
                String newF = CLASSNAME + "." + allocateShim("new__" + actor.actorName());
                String canF = CLASSNAME + "." + allocateShim("can__" + actor.actorName());
                NewActor impl = new NewActor(functionType, actor, canF, newF);
                impls.add(impl);
                return impl;
            }

            @Override
            public Object innerCallFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer) {
                String funcName = awaitConsumer.identifier().toString();
                String newF = CLASSNAME + "." + allocateShim("new__" + funcName);
                String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
                String outerClassName = OutTopLevelClass.qualifiedClassNameOf(func);
                boolean hasAwait = new HasAwait(ts, awaitConsumer).result();
                CallInnerActor impl = new CallInnerActor(
                        functionType, outerClassName, awaitConsumer, canF, newF, hasAwait);
                impls.add(impl);
                return impl;
            }

            @Override
            public Object innerNewFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer) {
                String funcName = awaitConsumer.identifier().toString();
                String newF = CLASSNAME + "." + allocateShim("new__" + funcName);
                String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
                String outerClassName = OutTopLevelClass.qualifiedClassNameOf(func);
                NewInnerActor impl = new NewInnerActor(functionType, outerClassName, awaitConsumer, canF, newF);
                impls.add(impl);
                return impl;
            }
        });
        actorTable.define(actorType);
    }

    public String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        shims.put(shimName, count);
        return shimName + "__" + count;
    }

    public String combineNewF(String funcName, int paramsCount, List<FunctionType.Invoked> invokeds) {
        String cNewF = allocateShim("cnew__" + funcName);
        g.__("public static Promise "
        ).__(cNewF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType.Invoked invoked : invokeds) {
                FunctionType funcType = invoked.function();
                if (!(funcType.attachment() instanceof FunctionImpl)) {
                    throw new IllegalStateException("no implementation attached to function: " + funcType);
                }
                FunctionImpl implEntry = (FunctionImpl) funcType.attachment();
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
            g.__("throw new DexRuntimeException(\"no implementation of "
            ).__(funcName
            ).__(" accepted the invocation: \" + java.util.Arrays.asList");
            InvokeParams.$(g, paramsCount, false);
            g.__(new Line(");"));
        }));
        g.__(new Line("}"));
        return CLASSNAME + "." + cNewF;
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
        String callF = CLASSNAME + "." + allocateShim("call__" + funcName);
        String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
        List<Type> params = ts.resolveType(javaFunction.getParameterTypes());
        Type ret = ts.resolveType(javaFunction.getReturnType());
        FunctionType functionType = new FunctionType(funcName, params, ret);
        ts.defineFunction(functionType);
        CallJavaFunction impl = new CallJavaFunction(functionType, javaFunction, canF, callF);
        impls.add(impl);
        functionType.attach(impl);
    }

    public static String stripPrefix(String f) {
        if (!f.startsWith(OutShim.CLASSNAME)) {
            throw new IllegalArgumentException();
        }
        return f.substring(OutShim.CLASSNAME.length() + 1);
    }

    public void importJavaClass(Class clazz) {
        new JavaClassType(ts, this, clazz);
    }
}
