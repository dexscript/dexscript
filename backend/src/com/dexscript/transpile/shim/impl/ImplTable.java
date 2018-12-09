package com.dexscript.transpile.shim.impl;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.ActorType;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ImplTable {

    private final TypeSystem ts;
    private final List<ActorEntry> actors = new ArrayList<>();
    private final List<InnerActorEntry> innerActors = new ArrayList<>();
    private final List<JavaFunctionEntry> javaFunctions = new ArrayList<>();

    public ImplTable(TypeSystem ts) {
        this.ts = ts;
    }

    public Iterable<ActorEntry> actors() {
        return actors;
    }

    public Iterable<InnerActorEntry> innerActors() {
        return innerActors;
    }

    public Iterable<JavaFunctionEntry> javaFunctions() {
        return javaFunctions;
    }

}
