package com.dexscript.type.composite;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.TypeSystem;

public class FakeActorTypeImpl implements ActorType.Impl {

    private final TypeSystem ts = new TypeSystem();

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public void addActorType(ActorType actorType) {

    }

    @Override
    public Object callActor(FunctionType expandedFunc, DexActor actor) {
        return new Object();
    }

    @Override
    public Object newActor(FunctionType expandedFunc, DexActor actor) {
        return new Object();
    }

    @Override
    public Object newInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer) {
        return new Object();
    }

    @Override
    public Object callInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer) {
        return new Object();
    }
}
