package com.dexscript.type.composite;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
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
import java.util.Map;

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
        if (typeArgs == null) {
            ts.defineType(this);
        }
    }

    public DexActor actor() {
        return actor;
    }

    @Override
    public @NotNull String name() {
        return actor.actorName();
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
        TypeTable localTypeTable = new TypeTable(ts);
        for (int i = 0; i < actor.typeParams().size(); i++) {
            String name = actor.typeParams().get(i).paramName().toString();
            localTypeTable.define(pkg(), name, typeArgs.get(i));
        }
        Map<DexElement, TypeTable> typeTableMap = new HashMap<>();
        typeTableMap.put(actor, localTypeTable);
        functions = new ArrayList<>();
        new AwaitConsumerCollector(typeTableMap).visit(actor.blk());
        functions.add(consumeFunc(typeTableMap));
        functions.add(callFunc(typeTableMap));
        functions.add(newFunc(typeTableMap));
        return functions;
    }

    private FunctionType callFunc(Map<DexElement, TypeTable> typeTableMap) {
        FunctionType functionType = new FunctionType(ts, actor().functionName(), typeTableMap, actor.sig());
        functionType.implProvider(expandedFunc -> impl.callActor(expandedFunc, actor));
        return functionType;
    }

    private FunctionType newFunc(Map<DexElement, TypeTable> typeTableMap) {
        FunctionType functionType = new FunctionType(ts, "New__", typeTableMap, actor.newFuncSig());
        functionType.implProvider(expandedFunc -> impl.newActor(expandedFunc, actor));
        return functionType;
    }

    private FunctionType consumeFunc(Map<DexElement, TypeTable> typeTableMap) {
        DType ret = InferType.$(ts, typeTableMap, actor.sig().ret());
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

        private final Map<DexElement, TypeTable> typeTableMap;

        public AwaitConsumerCollector(Map<DexElement, TypeTable> typeTableMap) {
            this.typeTableMap = typeTableMap;
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
            functions.add(callFunc(awaitConsumer));
//            functions.add(newFunc(awaitConsumer));
        }

//        @NotNull
//        private FunctionType newFunc(DexAwaitConsumer awaitConsumer) {
//            InnerActorType nestedActor = new InnerActorType(ts, awaitConsumer);
//            List<FunctionParam> headParams = new ArrayList<>();
//            String funcName = awaitConsumer.identifier().toString();
//            headParams.add(new FunctionParam("actor", new StringLiteralType(ts, funcName)));
//            headParams.add(new FunctionParam("self", ActorType.this));
//            DexSig sig = awaitConsumer.produceSig();
//            FunctionType functionType = new FunctionType(ts, "New__", typeTableMap, headParams, sig);
//            functionType.implProvider(expandedFunc -> impl.newInnerActor(expandedFunc, actor, awaitConsumer));
//            return functionType;
//        }

        @NotNull
        private FunctionType callFunc(DexAwaitConsumer awaitConsumer) {
            String funcName = awaitConsumer.identifier().toString();
            FunctionType functionType = new FunctionType(ts, funcName, typeTableMap, awaitConsumer.callFuncSig());
            functionType.implProvider(expandedFunc -> impl.callInnerActor(expandedFunc, actor, awaitConsumer));
            return functionType;
        }
    }
}
