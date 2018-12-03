package com.dexscript.denotation;

import java.util.List;

public class TypeFunction extends Type {

    private final String name;
    private final List<Type> params;
    private final Type ret;

    public TypeFunction(String name, List<Type> params, Type ret) {
        super("Object");
        this.name = name;
        this.params = params;
        this.ret = ret;
    }

    public String name() {
        return name;
    }
}
