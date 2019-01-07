package com.dexscript.type.composite;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.type.core.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActorType implements NamedType, GenericType, CompositeType {

    static {
        InferTypeTable.register(DexActor.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            typeTableMap.put(elem, null);
            return InferTypeTable.$(ts, typeTableMap, elem.sig());
        });
        InferTypeTable.register(DexAwaitConsumer.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            typeTableMap.put(elem, null);
            return InferTypeTable.$(ts, typeTableMap, elem.produceSig());
        });
    }

    public static void init() {
    }

    public interface Impl {
        TypeSystem typeSystem();

        void addActorType(ActorType actorType);

        Object callActor(FunctionType expandedFunc, DexActor actor);

        Object newActor(FunctionType expandedFunc, DexActor actor);

        Object newInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer);

        Object callInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer);
    }

    private final TypeSystem ts;
    private final Impl impl;
    private final DexActor actor;
    private List<DType> typeArgs;
    private List<FunctionType> members;
    private List<FunctionType> functions;
    private List<DType> typeParams;

    public ActorType(Impl impl, DexActor actor) {
        this(impl, actor, null);
    }

    public ActorType(Impl impl, DexActor actor, List<DType> typeArgs) {
        this.typeArgs = typeArgs;
        this.actor = actor;
        this.impl = impl;
        this.ts = impl.typeSystem();
        ts.lazyDefineFunctions(this);
        impl.addActorType(this);
    }

    public DexActor actor() {
        return actor;
    }

    @Override
    public @NotNull String name() {
        return actor.identifier().toString();
    }

    @Override
    public DexPackage pkg() {
        return actor.pkg();
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new ActorType(impl, actor, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexTypeParam typeParam : actor.typeParams()) {
                typeParams.add(InferType.$(ts, typeParam.paramType()));
            }
        }
        return typeParams;
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        TypeTable localTypeTable = new TypeTable(ts, actor.typeParams());
        members = new ArrayList<>();
        members.add(consumeFunc(localTypeTable));
        new AwaitConsumerCollector(localTypeTable).visit(actor.blk());
        functions = new ArrayList<>(members);
        functions.add(callFunc(localTypeTable));
        functions.add(newFunc(localTypeTable));
        return functions;
    }

    public FunctionType callFunc(TypeTable localTypeTable) {
        FunctionType functionType = new FunctionType(ts, name(), localTypeTable, actor.sig());
        functionType.implProvider(expandedFunc -> impl.callActor(expandedFunc, actor));
        return functionType;
    }

    public FunctionType newFunc(TypeTable localTypeTable) {
        List<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("actor", new StringLiteralType(ts, name())));
        for (DexParam param : actor.sig().params()) {
            String name = param.paramName().toString();
            DType type = InferType.$(ts, localTypeTable, param.paramType());
            params.add(new FunctionParam(name, type));
        }
        FunctionType functionType = new FunctionType(ts, "New__", params, this);
        functionType.implProvider(expandedFunc -> impl.newActor(expandedFunc, actor));
        return functionType;
    }

    private FunctionType consumeFunc(TypeTable localTypeTable) {
        DType ret = InferType.$(ts, localTypeTable, actor.sig().ret());
        ArrayList<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("self", this));
        return new FunctionType(ts, "Consume__", params, ret);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType thatObj) {
        return ts.functionTable().isAssignable(ctx, this, thatObj);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return name();
    }

    public DexActor elem() {
        return actor;
    }

    private class AwaitConsumerCollector implements DexElement.Visitor {

        private final TypeTable localTypeTable;

        public AwaitConsumerCollector(TypeTable localTypeTable) {
            this.localTypeTable = localTypeTable;
        }

        @Override
        public void visit(DexElement elem) {
            if (elem instanceof DexBlock || elem instanceof DexAwaitStmt) {
                elem.walkDown(this);
                return;
            }
            if (elem instanceof DexAwaitConsumer) {
                visitAwaitConsumer((DexAwaitConsumer) elem);
            }
        }

        private void visitAwaitConsumer(DexAwaitConsumer awaitConsumer) {
            members.add(callFunc(awaitConsumer));
            members.add(newFunc(awaitConsumer));
        }

        @NotNull
        private FunctionType newFunc(DexAwaitConsumer awaitConsumer) {
            InnerActorType nestedActor = new InnerActorType(ts, awaitConsumer);
            List<FunctionParam> params = new ArrayList<>();
            String funcName = awaitConsumer.identifier().toString();
            params.add(new FunctionParam("actor", new StringLiteralType(ts, funcName)));
            params.add(new FunctionParam("self", ActorType.this));
            DexSig sig = awaitConsumer.produceSig();
            for (DexParam param : sig.params()) {
                String paramName = param.paramName().toString();
                DType paramType = InferType.$(ts, localTypeTable, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            FunctionType functionType = new FunctionType(ts, "New__", params, nestedActor);
            functionType.implProvider(expandedFunc -> impl.newInnerActor(expandedFunc, actor, awaitConsumer));
            return functionType;
        }

        @NotNull
        private FunctionType callFunc(DexAwaitConsumer awaitConsumer) {
            DexSig sig = awaitConsumer.produceSig();
            String funcName = awaitConsumer.identifier().toString();
            DType ret = InferType.$(ts, localTypeTable, sig.ret());
            List<FunctionParam> params = new ArrayList<>();
            params.add(new FunctionParam("self", ActorType.this));
            for (DexParam param : sig.params()) {
                String paramName = param.paramName().toString();
                DType paramType = InferType.$(ts, localTypeTable, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            FunctionType functionType = new FunctionType(ts, funcName, params, ret);
            functionType.implProvider(expandedFunc -> impl.callInnerActor(expandedFunc, actor, awaitConsumer));
            return functionType;
        }
    }
}
