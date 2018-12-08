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

    private final TopLevelTypeTable typeTable;
    private final FunctionTable functionTable;
    private final DexInterface inf;
    private List<FunctionType> members;

    public InterfaceType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable, @NotNull DexInterface inf) {
        super(inf.identifier().toString(), "Object");
        typeTable.define(this);
        functionTable.lazyDefine(this);
        this.typeTable = typeTable;
        this.functionTable = functionTable;
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
            params.add(ResolveType.$(typeTable, param.paramType()));
        }
        Type ret = ResolveType.$(typeTable, infFunction.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    private void addInfMethod(DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(ResolveType.$(typeTable, param.paramType()));
        }
        Type ret = ResolveType.$(typeTable, infMethod.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        if (super.isAssignableFrom(that)) {
            return true;
        }
        return functionTable.isAssignableFrom(this, that);
    }

    @Override
    public String toString() {
        return name();
    }
}
