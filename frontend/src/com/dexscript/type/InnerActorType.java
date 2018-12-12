package com.dexscript.type;

import com.dexscript.ast.stmt.DexAwaitConsumer;

import java.util.ArrayList;
import java.util.List;

public class InnerActorType extends Type implements FunctionsProvider {

    private final TypeTable typeTable;
    private final DexAwaitConsumer awaitConsumer;
    private final FunctionTable functionTable;
    private List<FunctionType> members;

    public InnerActorType(TypeTable typeTable, FunctionTable functionTable, DexAwaitConsumer awaitConsumer) {
        super("com.dexscript.runtime.Promise");
        functionTable.lazyDefine(this);
        this.typeTable = typeTable;
        this.awaitConsumer = awaitConsumer;
        this.functionTable = functionTable;
    }

    @Override
    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        members = new ArrayList<>();
        members.add(consumeFunc());
        return members;
    }

    private FunctionType consumeFunc() {
        Type ret = ResolveType.$(typeTable, awaitConsumer.produceSig().ret());
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, ret);
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type thatObj) {
        for (FunctionType member : functions()) {
            if (!functionTable.isDefined(ctx, member)) {
                return false;
            }
        }
        return true;
    }
}
