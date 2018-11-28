package com.dexscript.resolve;

import com.dexscript.ast.core.DexElement;

import java.util.List;

public class Denotation {

    private final String name;
    private final Denotation type;

    public Denotation(String name, Denotation type) {
        this.name = name;
        this.type = type;
    }

    public final String name() {
        return name;
    }

    public final Denotation type() {
        return type;
    }

    public static class Value extends Denotation {

        private final DexElement referenced;

        public Value(String name, Type type, DexElement referenced) {
            super(name, type);
            this.referenced = referenced;
            if (type == null) {
                throw new IllegalArgumentException("must specify type for value denotation");
            }
        }

        public final DexElement referenced() {
            return referenced;
        }
    }

    public static class Type extends Denotation {

        private final DexElement referenced;
        private final String javaClassName;
        private final List<Type> args;
        private final Type ret;

        public Type(String name, TypeKind type, String javaClassName) {
            this(name, type, javaClassName, null, null, null);
        }

        public Type(String name, TypeKind type, String javaClassName, DexElement referenced, List<Type> args, Type ret) {
            super(name, type);
            this.args = args;
            this.referenced = referenced;
            this.ret = ret;
            this.javaClassName = javaClassName;
            if (type == null) {
                throw new IllegalArgumentException("must specify kind for type denotation");
            }
        }

        public DexElement referenced() {
            return referenced;
        }

        public String javaClassName() {
            return javaClassName;
        }

        public List<Type> args() {
            return args;
        }

        public Type ret() {
            return ret;
        }

        @Override
        public String toString() {
            return name();
        }

        public boolean isAssignableFrom(Denotation.Type that) {
            return this.equals(that);
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
        public static final TypeKind INTERFACE = new TypeKind("interface");

        public TypeKind(String name) {
            super(name, null);
        }
    }

    public static class Error extends Denotation implements DexSemanticError {

        private final DexElement occurredAt;
        private final String message;

        public Error(String name, DexElement occurredAt, String message) {
            super(name, null);
            this.occurredAt = occurredAt;
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }

        @Override
        public DexElement occurredAt() {
            return occurredAt;
        }
    }
}
