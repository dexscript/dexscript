package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMember;
import com.dexscript.ast.inf.DexInfMethod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceType extends TopLevelType implements FunctionsProvider {

    public interface ResolveType {
        Type resolveType(String name);
    }

    public interface ResolveFunction {
        boolean isDefined(FunctionType function);
    }

    private final ResolveType resolveType;
    private final ResolveFunction resolveFunction;
    private final DexInterface inf;
    private List<FunctionType> members;

    public InterfaceType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable, @NotNull DexInterface inf) {
        super(inf.identifier().toString(), "Object");
        typeTable.define(this);
        functionTable.lazyDefine(this);
        this.resolveType = typeTable;
        this.resolveFunction = functionTable;
        this.inf = inf;
    }

    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        members = new ArrayList<>();
        for (DexInfMember member : inf.members()) {
            if (member instanceof DexInfMethod) {
                addInfMethod((DexInfMethod) member);
            } else if (member instanceof DexInfFunction) {
                addInfFunction((DexInfFunction) member);
            }
        }
        return members;
    }

    private void addInfFunction(DexInfFunction infFunction) {
        String name = infFunction.identifier().toString();
        List<Type> params = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            params.add(resolveType.resolveType(param.paramType().toString()));
        }
        Type ret = resolveType.resolveType(infFunction.sig().ret().toString());
        members.add(new FunctionType(name, params, ret));
    }

    private void addInfMethod(DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(resolveType.resolveType(param.paramType().toString()));
        }
        Type ret = resolveType.resolveType(infMethod.sig().ret().toString());
        members.add(new FunctionType(name, params, ret));
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