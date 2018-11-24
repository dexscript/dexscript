package com.dexscript.resolve;

import com.dexscript.ast.core.DexElement;

import java.util.List;

public class Denotation {

    public final String name;
    public final Denotation type;

    public Denotation(String name, Denotation type) {
        this.name = name;
        this.type = type;
    }

    public static class Value extends Denotation {

        public final DexElement elem;

        public Value(String name, Type type, DexElement elem) {
            super(name, type);
            this.elem = elem;
            if (type == null) {
                throw new IllegalArgumentException("must specify type for value denotation");
            }
        }
    }

    public static class Type extends Denotation {

        public final DexElement elem;
        public final String javaClassName;
        public final List<Type> args;
        public final Type ret;

        public Type(String name, TypeKind type, String javaClassName) {
            this(name, type, javaClassName, null, null, null);
        }

        public Type(String name, TypeKind type, String javaClassName, DexElement elem, List<Type> args, Type ret) {
            super(name, type);
            this.args = args;
            this.elem = elem;
            this.ret = ret;
            this.javaClassName = javaClassName;
            if (type == null) {
                throw new IllegalArgumentException("must specify kind for type denotation");
            }
        }
    }

    public static Type javaClass(String name, String javaClassName) {
        return new Type(name, TypeKind.JAVA, javaClassName);
    }

    public static Type function(String name, DexElement elem, List<Type> args, Type ret) {
        return new Type(name, TypeKind.FUNCTION, "Object", elem, args, ret);
    }

    public static class TypeKind extends Denotation {

        public static final TypeKind JAVA = new TypeKind("java");
        public static final TypeKind FUNCTION = new TypeKind("function");

        public TypeKind(String name) {
            super(name, null);
        }
    }
}
