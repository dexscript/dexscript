package com.dexscript.resolve;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMember;

import java.util.ArrayList;
import java.util.List;

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

        protected abstract Type expand(int level);

        protected abstract Type expanded();
    }

    public static class BuiltinType extends Type {

        public BuiltinType(String name, String javaClassName) {
            super(name, TypeKind.JAVA, javaClassName);
        }

        @Override
        protected BuiltinType expand(int level) {
            return this;
        }

        @Override
        protected Type expanded() {
            return this;
        }
    }

    public static class FunctionType extends Type {

        private final DexElement definedBy;
        private final List<Type> params;
        private final Type ret;
        private Type expanded;

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
        public boolean isAssignableFrom(Type that) {
            return expanded().isAssignableFrom(that.expanded());
        }

        @Override
        protected Type expand(int level) {
            if (level >= TYPE_SYSTEM_MAX_RECURSION) {
                return new LeafExpandedFunctionType(name());
            }
            List<Type> expandedParams = new ArrayList<>();
            for (Type param : params()) {
                expandedParams.add(param.expand(level + 1));
            }
            Type expandedRet = ret().expand(level + 1);
            return new ExpandedFunctionType(name(), expandedParams, expandedRet);
        }

        @Override
        protected Type expanded() {
            if (expanded == null) {
                expanded = expand(0);
            }
            return expanded;
        }
    }

    public static class CallType extends Type {

        private final List<Type> args;

        public CallType(String name, List<Type> args) {
            super(name, TypeKind.CALL, null);
            this.args = args;
        }

        public List<Type> args() {
            return args;
        }

        @Override
        protected Type expand(int level) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Type expanded() {
            throw new UnsupportedOperationException();
        }
    }

    public static class InterfaceType extends Type {

        private final Resolve resolve;
        private final DexInterface definedBy;
        private List<FunctionType> members;
        private Type expanded;

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
            return expanded().isAssignableFrom(that.expanded());
        }

        @Override
        protected Type expand(int level) {
            List<ExpandedFunctionType> expandedMembers = new ArrayList<>();
            for (FunctionType member : members()) {
                expandedMembers.add((ExpandedFunctionType) member.expand(level + 1));
            }
            return new ExpandedInterfaceType(name(), expandedMembers);
        }

        @Override
        protected Type expanded() {
            if (expanded == null) {
                expanded = expand(0);
            }
            return expanded;
        }
    }

    private static class ExpandedInterfaceType extends Type {

        private final List<ExpandedFunctionType> members;

        public ExpandedInterfaceType(String name, List<ExpandedFunctionType> members) {
            super(name, TypeKind.INTERFACE, "Object");
            this.members = members;
        }

        @Override
        protected Type expand(int level) {
            return this;
        }

        @Override
        protected Type expanded() {
            return this;
        }

        @Override
        public boolean isAssignableFrom(Type thatObj) {
            if (!(thatObj instanceof ExpandedInterfaceType)) {
                return false;
            }
            // name does not matter
            ExpandedInterfaceType that = (ExpandedInterfaceType) thatObj.expanded();
            for (ExpandedFunctionType member : members) {
                if (!that.contains(member)) {
                    return false;
                }
            }
            return true;
        }

        private boolean contains(ExpandedFunctionType that) {
            for (ExpandedFunctionType member : members) {
                if (member.isAssignableFrom(that)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class ExpandedFunctionType extends Type {

        private final List<Type> params;
        private final Type ret;

        public ExpandedFunctionType(String name, List<Type> params, Type ret) {
            super(name, TypeKind.FUNCTION, "Object");
            this.params = params;
            this.ret = ret;
        }

        @Override
        protected Type expand(int level) {
            return this;
        }

        @Override
        protected Type expanded() {
            return this;
        }

        @Override
        public boolean isAssignableFrom(Type thatObj) {
            if (!(thatObj instanceof ExpandedFunctionType)) {
                return false;
            }
            ExpandedFunctionType that = (ExpandedFunctionType) thatObj;
            if (!name().equals(that.name())) {
                return false;
            }
            if (params.size() != that.params.size()) {
                return false;
            }
            for (int i = 0; i < params.size(); i++) {
                Type thisParam = params.get(i);
                Type thatParam = that.params.get(i);
                if (!thatParam.isAssignableFrom(thisParam)) {
                    return false;
                }
            }
            return that.ret.isAssignableFrom(ret);
        }
    }

    private static final class LeafExpandedFunctionType extends ExpandedFunctionType {

        public LeafExpandedFunctionType(String name) {
            super(name, null, null);
        }

        @Override
        public boolean isAssignableFrom(Type thatObj) {
            return thatObj instanceof LeafExpandedFunctionType;
        }
    }

    public static class TypeKind extends Denotation {

        public static final TypeKind JAVA = new TypeKind("java");
        public static final TypeKind FUNCTION = new TypeKind("function");
        public static final TypeKind CALL = new TypeKind("call");
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
