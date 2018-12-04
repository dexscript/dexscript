package com.dexscript.type;

import com.dexscript.ast.core.DexElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FunctionType extends Type {

    @NotNull
    private final String name;

    @NotNull
    private final List<Type> params;

    @NotNull
    private final Type ret;

    private final DexElement definedBy;

    public FunctionType(@NotNull String name, @NotNull List<Type> params, @NotNull Type ret) {
        this(null, name, params, ret);
    }

    public FunctionType(DexElement definedBy, @NotNull String name, @NotNull List<Type> params, @NotNull Type ret) {
        super("Object");
        this.definedBy = definedBy;
        this.name = name;
        this.params = params;
        this.ret = ret;
    }

    @NotNull
    public final String name() {
        return name;
    }

    @NotNull
    public List<Type> params() {
        return params;
    }

    @NotNull
    public Type ret() {
        return ret;
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (super.isAssignableFrom(thatObj)) {
            return true;
        }
        if (!(thatObj instanceof FunctionType)) {
            return false;
        }
        FunctionType that = (FunctionType) thatObj;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.params.size() != that.params.size()) {
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            Type thisParam = this.params.get(i);
            Type thatParam = that.params.get(i);
            if (!thatParam.isAssignableFrom(thisParam)) {
                return false;
            }
        }
        return this.ret.isAssignableFrom(that.ret);
    }

    @Override
    protected Type expand(Map<Type, Type> lookup) {
        List<Type> expandedParams = new ArrayList<>();
        for (Type param : params()) {
            expandedParams.add(param.expand(lookup));
        }
        Type expandedRet = ret().expand(lookup);
        return new FunctionType(name, expandedParams, expandedRet);
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(name);
        msg.append('(');
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                msg.append(", ");
            }
            msg.append(params.get(i).toString());
        }
        msg.append(") => ");
        msg.append(ret.toString());
        return msg.toString();
    }

    public DexElement definedBy() {
        return definedBy;
    }
}
