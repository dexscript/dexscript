package com.dexscript.denotation;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMember;
import com.dexscript.ast.inf.DexInfMethod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeInterface extends TopLevelType implements FunctionsProvider {

    public interface ResolveType {
        Type resolveType(DexReference ref);
    }

    public interface ResolveFunction {
        boolean isDefined(TypeFunction function);
    }

    private final ResolveType resolveType;
    private final ResolveFunction resolveFunction;
    private final DexInterface inf;
    private List<TypeFunction> members;

    public TypeInterface(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable, @NotNull DexInterface inf) {
        super(inf.identifier().toString(), "Object");
        typeTable.define(this);
        functionTable.lazyDefine(this);
        this.resolveType = typeTable;
        this.resolveFunction = functionTable;
        this.inf = inf;
    }

    public List<TypeFunction> functions() {
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
            params.add(resolveType.resolveType(param.paramType()));
        }
        Type ret = resolveType.resolveType(infFunction.sig().ret());
        members.add(new TypeFunction(name, params, ret));
    }

    private void addInfMethod(DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(resolveType.resolveType(param.paramType()));
        }
        Type ret = resolveType.resolveType(infMethod.sig().ret());
        members.add(new TypeFunction(name, params, ret));
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (this.equals(thatObj)) {
            return true;
        }
        Map<Type, Type> lookup = new HashMap<>();
        lookup.put(this, new TypeSame(thatObj));
        for (TypeFunction member : functions()) {
            if (!resolveFunction.isDefined((TypeFunction) member.expand(lookup))) {
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
