package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.ast.stmt.DexBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActorType implements NamedType, GenericType, FunctionsProvider {

    public interface ImplProvider {
        Object callFunc(FunctionType functionType, DexActor func);

        Object newFunc(FunctionType functionType, DexActor func);

        Object innerCallFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer);

        Object innerNewFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer);
    }

    private final TypeTable typeTable;
    private final FunctionTable functionTable;
    private final DexActor actor;
    private List<Type> typeArgs;
    private List<FunctionType> members;
    private List<FunctionType> functions;
    private List<Type> typeParams;
    private ImplProvider implProvider;

    public ActorType(TypeTable typeTable, FunctionTable functionTable, DexActor actor, ImplProvider implProvider) {
        this(typeTable, functionTable, actor, implProvider, null);
    }

    public ActorType(TypeTable typeTable, FunctionTable functionTable, DexActor actor,
                     ImplProvider implProvider, List<Type> typeArgs) {
        functionTable.lazyDefine(this);
        this.typeArgs = typeArgs;
        this.implProvider = implProvider;
        this.actor = actor;
        this.typeTable = typeTable;
        this.functionTable = functionTable;
    }

    @Override
    public @NotNull String name() {
        return actor.identifier().toString();
    }

    @Override
    public String javaClassName() {
        return "com.dexscript.runtime.Promise";
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new ActorType(typeTable, functionTable, actor, implProvider, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexTypeParam typeParam : actor.typeParams()) {
                typeParams.add(ResolveType.$(typeTable, typeParam.paramType()));
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
        TypeTable localTypeTable = new TypeTable(typeTable);
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
        FunctionType functionType = new FunctionType(name(), params, ret);
        functionType.attach((FunctionType.LazyAttachment) () -> implProvider.callFunc(functionType, actor));
        return functionType;
    }

    public FunctionType newFunc(TypeTable localTypeTable) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(new StringLiteralType(name()));
        for (DexParam param : actor.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        FunctionType functionType = new FunctionType("New__", params, this);
        functionType.attach((FunctionType.LazyAttachment) () -> implProvider.newFunc(functionType, actor));
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
            if (!functionTable.isDefined(ctx, member)) {
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
            InnerActorType nestedActor = new InnerActorType(typeTable, functionTable, awaitConsumer);
            ArrayList<Type> params = new ArrayList<>();
            String funcName = awaitConsumer.identifier().toString();
            params.add(new StringLiteralType(funcName));
            params.add(ActorType.this);
            DexSig sig = awaitConsumer.produceSig();
            for (DexParam param : sig.params()) {
                params.add(ResolveType.$(localTypeTable, param.paramType()));
            }
            FunctionType functionType = new FunctionType("New__", params, nestedActor);
            functionType.attach((FunctionType.LazyAttachment) () -> implProvider.innerNewFunc(
                    functionType, actor, awaitConsumer));
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
            functionType.attach((FunctionType.LazyAttachment) () -> implProvider.innerCallFunc(
                    functionType, actor, awaitConsumer));
            return functionType;
        }
    }
}
