package com.dexscript.transpile.type.actor;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.*;

import java.util.ArrayList;
import java.util.List;

public class InnerActorType implements DType, FunctionsType {

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
        DType ret = ResolveType.$(ts, null, awaitConsumer.produceSig().ret());
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        return new FunctionType(ts, "Consume__", params, ret);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return ts.functionTable().isSubType(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
