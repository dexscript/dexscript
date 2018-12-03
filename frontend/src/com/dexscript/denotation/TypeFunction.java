package com.dexscript.denotation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TypeFunction extends Type {

    @NotNull
    private final String name;

    @NotNull
    private final List<Type> params;

    @NotNull
    private final Type ret;

    public TypeFunction(@NotNull String name, @NotNull List<Type> params, @NotNull Type ret) {
        super("Object");
        this.name = name;
        this.params = params;
        this.ret = ret;
    }

    @NotNull
    public final String name() {
        return name;
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (!(thatObj instanceof TypeFunction)) {
            return false;
        }
        TypeFunction that = (TypeFunction) thatObj;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.params.size() != that.params.size()) {
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            Type thisParam = this.params.get(i);
            Type thatParam = that.params.get(i);
            if (!thisParam.isAssignableFrom(thatParam)) {
                return false;
            }
        }
        return this.ret.isAssignableFrom(that.ret);
    }
}
