package com.dexscript.type;

import com.dexscript.ast.func.DexAwaitConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnerActorType extends Type implements FunctionsProvider {

    private final TopLevelTypeTable typeTable;
    private final DexAwaitConsumer awaitConsumer;
    private final FunctionTable functionTable;
    private List<FunctionType> members;

    public InnerActorType(TopLevelTypeTable typeTable, FunctionTable functionTable, DexAwaitConsumer awaitConsumer) {
        super("Result");
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
        return new FunctionType(awaitConsumer,"Consume__", params, ret);
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (super.isAssignableFrom(thatObj)) {
            return true;
        }
        if (thatObj instanceof SameType) {
            return false;
        }
        Map<Type, Type> lookup = new HashMap<>();
        lookup.put(this, new SameType(thatObj));
        for (FunctionType member : functions()) {
            FunctionType expandedMember = (FunctionType) member.expand(lookup);
            if (!functionTable.isDefined(expandedMember)) {
                return false;
            }
        }
        return true;
    }
}