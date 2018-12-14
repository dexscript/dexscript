package com.dexscript.transpile.type.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActorType implements NamedType, GenericType, FunctionsProvider {

    private final TypeSystem ts;
    private final OutShim oShim;
    private final DexActor actor;
    private List<Type> typeArgs;
    private List<FunctionType> members;
    private List<FunctionType> functions;
    private List<Type> typeParams;

    public ActorType(OutShim oShim, DexActor actor) {
        this(oShim, actor, null);
    }

    public ActorType(OutShim oShim, DexActor actor, List<Type> typeArgs) {
        this.typeArgs = typeArgs;
        this.actor = actor;
        this.oShim = oShim;
        this.ts = oShim.typeSystem();
        ts.lazyDefineFunctions(this);
        oShim.javaTypes().add(qualifiedClassNameOf(actor), this);
    }

    public static String qualifiedClassNameOf(DexActor actor) {
        String packageName = actor.file().packageClause().identifier().toString();
        return packageName + "." + actor.actorName();
    }

    @Override
    public @NotNull String name() {
        return actor.identifier().toString();
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new ActorType(oShim, actor, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexTypeParam typeParam : actor.typeParams()) {
                typeParams.add(ts.resolveType(typeParam.paramType()));
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
        TypeTable localTypeTable = new TypeTable(ts.typeTable());
        for (int i = 0; i < actor.typeParams().size(); i++) {
            DexTypeParam typeParam = actor.typeParams().get(i);
            String typeParamName = typeParam.paramName().toString();
            localTypeTable.define(typeParamName, typeArgs.get(i));
        }
        members = new ArrayList<>();
        members.add(consumeFunc(localTypeTable));
        new AwaitConsumerCollector(localTypeTable).visit(actor.blk());
        functions = new ArrayList<>(members);
        functions.add(callFunc(localTypeTable));
        functions.add(newFunc(localTypeTable));
        return functions;
    }

    public FunctionType callFunc(TypeTable localTypeTable) {
        Type ret = ResolveType.$(localTypeTable, actor.sig().ret());
        ArrayList<Type> params = new ArrayList<>();
        for (DexParam param : actor.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        FunctionSig sig = new FunctionSig(ts.typeTable(), actor.sig());
        FunctionType functionType = new FunctionType(name(), params, ret, sig);
        functionType.attach((FunctionType.LazyAttachment) () -> new CallActor(oShim, functionType, actor));
        return functionType;
    }

    public FunctionType newFunc(TypeTable localTypeTable) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(new StringLiteralType(name()));
        for (DexParam param : actor.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        FunctionType functionType = new FunctionType("New__", params, this);
        functionType.attach((FunctionType.LazyAttachment) () -> new NewActor(oShim, functionType, actor));
        return functionType;
    }

    private FunctionType consumeFunc(TypeTable localTypeTable) {
        Type ret = ResolveType.$(localTypeTable, actor.sig().ret());
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, ret);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type thatObj) {
        functions();
        for (FunctionType member : members) {
            if (!ts.isFunctionDefined(ctx, member)) {
                return false;
            }
        }
        return true;
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
            ArrayList<Type> params = new ArrayList<>();
            String funcName = awaitConsumer.identifier().toString();
            params.add(new StringLiteralType(funcName));
            params.add(ActorType.this);
            DexSig sig = awaitConsumer.produceSig();
            for (DexParam param : sig.params()) {
                params.add(ResolveType.$(localTypeTable, param.paramType()));
            }
            FunctionType functionType = new FunctionType("New__", params, nestedActor);
            functionType.attach((FunctionType.LazyAttachment) () -> new NewInnerActor(
                    oShim, functionType, outerClassName, awaitConsumer));
            return functionType;
        }

        @NotNull
        private FunctionType callFunc(DexAwaitConsumer awaitConsumer) {
            DexSig sig = awaitConsumer.produceSig();
            String funcName = awaitConsumer.identifier().toString();
            Type ret = ResolveType.$(localTypeTable, sig.ret());
            ArrayList<Type> params = new ArrayList<>();
            params.add(ActorType.this);
            for (DexParam param : sig.params()) {
                params.add(ResolveType.$(localTypeTable, param.paramType()));
            }
            FunctionType functionType = new FunctionType(funcName, params, ret);
            functionType.attach((FunctionType.LazyAttachment) () -> new CallInnerActor(
                    oShim, functionType, outerClassName, awaitConsumer));
            return functionType;
        }
    }
}
