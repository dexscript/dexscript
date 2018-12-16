package com.dexscript.type;

import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.type.DexType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// part of FunctionType
public class FunctionSig {

    public abstract class Invoked {

        public abstract boolean compatible();

        public abstract boolean needRuntimeCheck();

        public abstract Type ret();

        public FunctionType function() {
            return functionType;
        }
    }

    public abstract class Incompatible extends Invoked {

        @Override
        public boolean compatible() {
            return false;
        }

        @Override
        public boolean needRuntimeCheck() {
            return false;
        }

        @Override
        public Type ret() {
            return BuiltinTypes.UNDEFINED;
        }
    }

    public class ArgumentsCountIncompatible extends Incompatible {

    }

    public class ArgumentIncompatible extends Incompatible {

        private final int index;

        public ArgumentIncompatible(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public class MissingTypeArgument extends Incompatible {

        private final PlaceholderType typeParam;

        public MissingTypeArgument(PlaceholderType typeParam) {
            this.typeParam = typeParam;
        }

        public PlaceholderType typeParam() {
            return typeParam;
        }
    }

    public class Compatible extends Invoked {

        private final boolean needRuntimeCheck;
        private final Type ret;

        public Compatible(boolean needRuntimeCheck, Type ret) {
            this.needRuntimeCheck = needRuntimeCheck;
            this.ret = ret;
        }

        @Override
        public boolean compatible() {
            return true;
        }

        @Override
        public boolean needRuntimeCheck() {
            return needRuntimeCheck;
        }

        @Override
        public Type ret() {
            return ret;
        }
    }

    private String description;
    private FunctionType functionType;
    private final List<PlaceholderType> typeParams;
    private final List<Type> params;
    private final Type ret;
    private final DexType retElem;

    public FunctionSig(List<Type> params, Type ret) {
        this.typeParams = Collections.emptyList();
        this.params = params;
        this.ret = ret;
        this.retElem = null;
    }

    public FunctionSig(List<PlaceholderType> typeParams, List<Type> params, Type ret, DexType retElem) {
        this.typeParams = typeParams == null ? Collections.emptyList() : typeParams;
        this.params = params;
        this.ret = ret;
        this.retElem = retElem;
    }

    public FunctionSig(TypeTable typeTable, DexSig sig) {
        this(typeTable, null, sig);
    }

    public FunctionSig(TypeTable typeTable, Type objectType, DexSig sig) {
        typeParams = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(typeTable);
        for (DexTypeParam typeParam : sig.typeParams()) {
            Type constraint = ResolveType.$(typeTable, typeParam.paramType());
            PlaceholderType placeholder = new PlaceholderType(typeParam.paramName().toString(), constraint);
            localTypeTable.define(placeholder);
            typeParams.add(placeholder);
        }
        params = new ArrayList<>();
        if (objectType != null) {
            params.add(objectType);
        }
        for (DexParam param : sig.params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        ret = ResolveType.$(localTypeTable, sig.ret());
        retElem = sig.ret();
    }

    public void reparent(FunctionType functionType) {
        this.functionType = functionType;
    }

    public List<Type> params() {
        return params;
    }

    public Type ret() {
        return ret;
    }

    public List<PlaceholderType> typeParams() {
        return typeParams;
    }

    Invoked invoke(TypeTable typeTable, Invocation ivc) {
        List<Type> args = ivc.args();
        List<Type> typeArgs = ivc.typeArgs();
        Type retHint = ivc.retHint();
        if (params.size() != args.size()) {
            return new ArgumentsCountIncompatible();
        }
        Map<Type, Type> sub = initSub(typeArgs);
        TypeComparisonContext ctx = new TypeComparisonContext(typeTable.comparisonCache(), sub);
        boolean needRuntimeCheck = false;
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            TypeComparisonContext subCtx = new TypeComparisonContext(ctx);
            boolean argMatched = param.isAssignableFrom(subCtx, arg);
            if (!argMatched) {
                needRuntimeCheck = true;
                subCtx.rollback();
                argMatched = arg.isAssignableFrom(subCtx, param);
            }
            if (!argMatched) {
                return new ArgumentIncompatible(i);
            }
            subCtx.commit();
        }
        if (retElem == null || typeParams == null) {
            return new Compatible(needRuntimeCheck, ret);
        }
        inferRetHint(retHint, ctx);
        for (PlaceholderType typeParam : typeParams) {
            if (!sub.containsKey(typeParam)) {
                return new MissingTypeArgument(typeParam);
            }
        }
        TypeTable localTypeTable = new TypeTable(typeTable);
        for (Map.Entry<Type, Type> entry : sub.entrySet()) {
            Type key = entry.getKey();
            if (key instanceof NamedType) {
                localTypeTable.define(((NamedType) key).name(), entry.getValue());
            }
        }
        Type expandedRet = ResolveType.$(localTypeTable, retElem);
        return new Compatible(needRuntimeCheck, expandedRet);
    }

    @NotNull
    private Map<Type, Type> initSub(List<Type> typeArgs) {
        Map<Type, Type> collector = new HashMap<>();
        if (typeArgs == null) {
            return collector;
        }
        if (typeArgs.isEmpty()) {
            return collector;
        }
        if (typeParams == null) {
            return collector;
        }
        if (typeParams.size() != typeArgs.size()) {
            return collector;
        }
        for (int i = 0; i < typeParams.size(); i++) {
            PlaceholderType typeParam = typeParams.get(i);
            Type typeArg = typeArgs.get(i);
            collector.put(typeParam, typeArg);
        }
        return collector;
    }

    private void inferRetHint(Type retHint, TypeComparisonContext ctx) {
        if (retHint != null) {
            TypeComparisonContext subCtx = new TypeComparisonContext(ctx);
            boolean assignable = retHint.isAssignableFrom(subCtx, ret);
            if (!assignable) {
                subCtx.rollback();
                assignable = ret.isAssignableFrom(subCtx, retHint);
            }
            if (assignable) {
                subCtx.commit();
            }
        }
    }

    @Override
    public String toString() {
        if (description != null) {
            return description;
        }
        StringBuilder desc = new StringBuilder();
        desc.append('(');
        boolean isFirst = true;
        for (PlaceholderType typeParam : typeParams()) {
            if (isFirst) {
                isFirst = false;
            } else {
                desc.append(", ");
            }
            desc.append(typeParam.description());
        }
        for (Type param : params) {
            if (isFirst) {
                isFirst = false;
            } else {
                desc.append(", ");
            }
            desc.append(param.toString());
        }
        desc.append("): ");
        desc.append(ret.toString());
        description = desc.toString();
        return description;
    }
}
