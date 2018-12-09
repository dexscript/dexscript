package com.dexscript.transpile.shim.impl;

import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.List;

public class ImplTable {

    private final TypeSystem ts;
    private final List<CallActor> actors = new ArrayList<>();
    private final List<CallInnerActor> innerActors = new ArrayList<>();
    private final List<CallJavaFunction> javaFunctions = new ArrayList<>();

    public ImplTable(TypeSystem ts) {
        this.ts = ts;
    }

    public Iterable<CallActor> actors() {
        return actors;
    }

    public Iterable<CallInnerActor> innerActors() {
        return innerActors;
    }

    public Iterable<CallJavaFunction> javaFunctions() {
        return javaFunctions;
    }

}
