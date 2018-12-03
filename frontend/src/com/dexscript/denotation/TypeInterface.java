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

public class TypeInterface extends TopLevelType implements FunctionGroup {

    public interface Resolve {
        Type resolveType(DexReference ref);
    }

    private final Resolve resolve;
    private final DexInterface inf;
    private List<TypeFunction> members;

    public TypeInterface(@NotNull Resolve resolve, @NotNull DexInterface inf) {
        super(inf.identifier().toString(), "Object");
        this.resolve = resolve;
        this.inf = inf;
    }

    public List<TypeFunction> members() {
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
            params.add(resolve.resolveType(param.paramType()));
        }
        Type ret = resolve.resolveType(infFunction.sig().ret());
        members.add(new TypeFunction(name, params, ret));
    }

    private void addInfMethod(DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(resolve.resolveType(param.paramType()));
        }
        Type ret = resolve.resolveType(infMethod.sig().ret());
        members.add(new TypeFunction(name, params, ret));
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (this.equals(thatObj)) {
            return true;
        }
        if (!(thatObj instanceof FunctionGroup)) {
            return false;
        }
        FunctionGroup that = (FunctionGroup) thatObj;
        Map<Type, Type> lookup = new HashMap<>();
        lookup.put(this, thatObj);
        for (TypeFunction member : members()) {
            if (!that.contains((TypeFunction) member.expand(lookup))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(TypeFunction function) {
        for (TypeFunction member : members()) {
            if (function.isAssignableFrom(member)) {
                return true;
            }
        }
        return false;
    }
}
