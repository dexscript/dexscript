package com.dexscript.transpile.type.actor;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.*;

import java.util.ArrayList;
import java.util.List;

public class InnerActorType implements Type, FunctionsProvider {

    private final DexAwaitConsumer awaitConsumer;
    private final TypeSystem ts;
    private List<FunctionType> members;

    public InnerActorType(TypeSystem ts, DexAwaitConsumer awaitConsumer) {
        ts.lazyDefineFunctions(this);
        this.ts = ts;
        this.awaitConsumer = awaitConsumer;
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
        Type ret = ts.resolveType(awaitConsumer.produceSig().ret());
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, ret);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type thatObj) {
        for (FunctionType member : functions()) {
            if (!ts.isFunctionDefined(ctx, member)) {
                return false;
            }
        }
        return true;
    }
}
