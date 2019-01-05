package com.dexscript.shim.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActorType implements NamedType, GenericType, CompositeType {

    private final TypeSystem ts;
    private final OutShim oShim;
    private final DexActor actor;
    private List<DType> typeArgs;
    private List<FunctionType> members;
    private List<FunctionType> functions;
    private List<DType> typeParams;

    public ActorType(OutShim oShim, DexActor actor) {
        this(oShim, actor, null);
    }

    public ActorType(OutShim oShim, DexActor actor, List<DType> typeArgs) {
        this.typeArgs = typeArgs;
        this.actor = actor;
        this.oShim = oShim;
        this.ts = oShim.typeSystem();
        ts.lazyDefineFunctions(this);
        oShim.javaTypes().add(qualifiedClassNameOf(actor), this);
    }

    public static String qualifiedClassNameOf(DexActor actor) {
        return "com.dexscript.transpiled." + actor.actorName();
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
        return new ActorType(oShim, actor, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexTypeParam typeParam : actor.typeParams()) {
                typeParams.add(ResolveType.$(ts, null, typeParam.paramType()));
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
        functionType.implProvider(expandedFunc -> new CallActor(oShim, expandedFunc, actor));
        return functionType;
    }

    public FunctionType newFunc(TypeTable localTypeTable) {
        List<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("actor", new StringLiteralType(ts, name())));
        for (DexParam param : actor.sig().params()) {
            String name = param.paramName().toString();
            DType type = ResolveType.$(ts, localTypeTable, param.paramType());
            params.add(new FunctionParam(name, type));
        }
        FunctionType functionType = new FunctionType(ts, "New__", params, this);
        functionType.implProvider(expandedFunc -> new NewActor(oShim, expandedFunc, actor));
        return functionType;
    }

    private FunctionType consumeFunc(TypeTable localTypeTable) {
        DType ret = ResolveType.$(ts, localTypeTable, actor.sig().ret());
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
        private final String outerClassName;

        public AwaitConsumerCollector(TypeTable localTypeTable) {
            this.localTypeTable = localTypeTable;
            outerClassName = qualifiedClassNameOf(actor);
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
                DType paramType = ResolveType.$(ts, localTypeTable, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            FunctionType functionType = new FunctionType(ts, "New__", params, nestedActor);
            functionType.implProvider(expandedFunc -> new NewInnerActor(
                    oShim, expandedFunc, outerClassName, awaitConsumer));
            return functionType;
        }

        @NotNull
        private FunctionType callFunc(DexAwaitConsumer awaitConsumer) {
            DexSig sig = awaitConsumer.produceSig();
            String funcName = awaitConsumer.identifier().toString();
            DType ret = ResolveType.$(ts, localTypeTable, sig.ret());
            List<FunctionParam> params = new ArrayList<>();
            params.add(new FunctionParam("self", ActorType.this));
            for (DexParam param : sig.params()) {
                String paramName = param.paramName().toString();
                DType paramType = ResolveType.$(ts, localTypeTable, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            FunctionType functionType = new FunctionType(ts, funcName, params, ret);
            functionType.implProvider(expandedFunc -> new CallInnerActor(
                    oShim, expandedFunc, outerClassName, awaitConsumer));
            return functionType;
        }
    }
}
