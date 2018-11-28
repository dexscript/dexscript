package com.dexscript.resolve;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Denotation {

    private static final int TYPE_SYSTEM_MAX_RECURSION = 20;
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

    public static abstract class Type extends Denotation {

        private final String javaClassName;

        public Type(String name, TypeKind type, String javaClassName) {
            super(name, type);
            this.javaClassName = javaClassName;
            if (type == null) {
                throw new IllegalArgumentException("must specify kind for type denotation");
            }
        }

        public String javaClassName() {
            return javaClassName;
        }

        @Override
        public String toString() {
            return name();
        }

        public boolean isAssignableFrom(Denotation.Type that) {
            return this.equals(that);
        }

        public Type expand(Map<Type, Type> lookup) {
            for (Map.Entry<Type, Type> entry : lookup.entrySet()) {
                if (this.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return this;
        }

        protected abstract boolean canProvide(String functionName, List<Type> args, Type ret);
    }

    public static class BuiltinType extends Type {

        public BuiltinType(String name, String javaClassName) {
            super(name, TypeKind.JAVA, javaClassName);
        }

        @Override
        protected boolean canProvide(String functionName, List<Type> args, Type ret) {
            return false;
        }
    }

    public static class FunctionType extends Type {

        private final DexElement definedBy;
        private final List<Type> params;
        private final Type ret;

        public FunctionType(String name, DexElement definedBy, List<Type> params, Type ret) {
            super(name, TypeKind.FUNCTION, "Object");
            this.definedBy = definedBy;
            this.params = params;
            this.ret = ret;
        }

        public DexElement definedBy() {
            return definedBy;
        }

        public List<Type> params() {
            return params;
        }

        public Type ret() {
            return ret;
        }

        @Override
        protected boolean canProvide(String thatName, List<Type> thatParams, Type thatRet) {
            if (!name().equals(thatName)) {
                return false;
            }
            if (params().size() != thatParams.size()) {
                return false;
            }
            for (int i = 0; i < params().size(); i++) {
                Type thisParam = params().get(i);
                Type thatParam = thatParams.get(i);
                if (!thatParam.isAssignableFrom(thisParam)) {
                    return false;
                }
            }
            return thatRet.isAssignableFrom(ret());
        }
    }

    public static class InterfaceType extends Type {

        private final Resolve resolve;
        private final DexInterface definedBy;
        private List<FunctionType> members;

        public InterfaceType(Resolve resolve, DexInterface definedBy) {
            super(definedBy.identifier().toString(), TypeKind.INTERFACE, "Object");
            this.resolve = resolve;
            this.definedBy = definedBy;
        }

        public final List<FunctionType> members() {
            if (members == null) {
                members = new ArrayList<>();
                for (DexInfMember member : definedBy.members()) {
                    if (member instanceof DexInfFunction) {
                        members.add(toFunctionType((DexInfFunction) member));
                    }
                }
            }
            return members;
        }

        private FunctionType toFunctionType(DexInfFunction member) {
            String functionName = member.identifier().toString();
            List<Type> paramTypes = new ArrayList<>();
            for (DexParam param : member.sig().params()) {
                Denotation.Type paramType = (Type) resolve.resolveType(param.paramType());
                paramTypes.add(paramType);
            }
            Denotation.Type retType = (Type) resolve.resolveType(member.sig().ret());
            return new FunctionType(functionName, member, paramTypes, retType);
        }

        public DexInterface definedBy() {
            return definedBy;
        }

        @Override
        public boolean isAssignableFrom(Type that) {
            if (this.equals(that)) {
                return true;
            }
            HashMap<Type, Type> lookup = new HashMap<>();
            lookup.put(this, that);
            for (FunctionType member : members()) {
                List<Type> expandedParams = new ArrayList<>();
                for (Type param : member.params()) {
                    expandedParams.add(param.expand(lookup));
                }
                Type expandedRet = member.ret().expand(lookup);
                if (resolve.canProvide(member.name(), expandedParams, expandedRet)) {
                    continue;
                }
                if (!that.canProvide(member.name(), expandedParams, expandedRet)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected boolean canProvide(String functionName, List<Type> params, Type ret) {
            for (FunctionType member : members()) {
                if (member.canProvide(functionName, params, ret)) {
                    return true;
                }
            }
            return false;
        }
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
            return message + " @ " + occurredAt.parent();
        }

        @Override
        public DexElement occurredAt() {
            return occurredAt;
        }
    }
}
