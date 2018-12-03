package com.dexscript.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorType extends TopLevelType implements FunctionsProvider {

    public interface ResolveType {
        Type resolveType(String name);
    }

    public interface ResolveFunction {
        boolean isDefined(FunctionType function);
    }

    private final ResolveType resolveType;
    private final ResolveFunction resolveFunction;
    private final DexFunction func;
    private List<FunctionType> members;

    public ActorType(ActorTable actorTable, FunctionTable functionTable, DexFunction func) {
        super(func.identifier().toString(), "Object");
        this.func = func;
        this.resolveType = actorTable;
        this.resolveFunction = functionTable;
        actorTable.define(this);
        functionTable.lazyDefine(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        members = new ArrayList<>();
        members.add(consumeFunc());
        List<FunctionType> functions = new ArrayList<>(members);
        functions.add(newFunc());
        return functions;
    }

    private FunctionType newFunc() {
        ArrayList<Type> params = new ArrayList<>();
        params.add(new StringLiteralType(name()));
        for (DexParam param : func.sig().params()) {
            params.add(resolveType.resolveType(param.paramType().toString()));
        }
        return new FunctionType("New__", params, this);
    }

    private FunctionType consumeFunc() {
        Type ret = resolveType.resolveType(func.sig().ret().toString());
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, ret);
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
            if (!resolveFunction.isDefined(expandedMember)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return name();
    }
}
