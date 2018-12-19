package com.dexscript.type;

import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// part of FunctionType
public class FunctionSig {

    public abstract class Invoked {

        public abstract boolean success();

        public abstract boolean needRuntimeCheck();

        public FunctionType function() {
            return func;
        }

        public abstract void dump();
    }

    public abstract class Incompatible extends Invoked {

        @Override
        public boolean success() {
            return false;
        }

        @Override
        public boolean needRuntimeCheck() {
            return false;
        }

    }

    public class ArgumentsCountIncompatible extends Incompatible {

        @Override
        public void dump() {
            System.out.println("arguments count incompatible");
        }
    }

    public class TypeArgumentsCountIncompatible extends Incompatible {

        @Override
        public void dump() {
            System.out.println("type arguments count incompatible");
        }
    }

    public class ArgumentIncompatible extends Incompatible {

        private final int index;
        private final IsAssignable paramArg;
        private final IsAssignable argParam;

        public ArgumentIncompatible(int index, IsAssignable paramArg, IsAssignable argParam) {

            this.index = index;
            this.paramArg = paramArg;
            this.argParam = argParam;
        }

        public int index() {
            return index;
        }

        public IsAssignable paramArg() {
            return paramArg;
        }

        public IsAssignable argParam() {
            return argParam;
        }

        @Override
        public void dump() {
            System.out.println("#" + index + " argument is incompatible");
            System.out.println("tried param=arg");
            paramArg.dump();
            System.out.println("tried arg=param");
            argParam.dump();
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

        @Override
        public void dump() {
            System.out.println("missing type argument: " + typeParam);
        }
    }

    public class Compatible extends Invoked {

        private final boolean needRuntimeCheck;
        private final FunctionType expandedFunction;

        public Compatible(boolean needRuntimeCheck, FunctionType expandedFunction) {
            this.needRuntimeCheck = needRuntimeCheck;
            this.expandedFunction = expandedFunction;
        }

        @Override
        public boolean success() {
            return true;
        }

        @Override
        public FunctionType function() {
            return expandedFunction;
        }

        @Override
        public void dump() {
        }

        @Override
        public boolean needRuntimeCheck() {
            return needRuntimeCheck;
        }

    }

    private final TypeSystem ts;
    private FunctionType func;
    private final List<PlaceholderType> typeParams;
    private final List<DType> params;
    private final DType ret;
    private final DexSig dexSig;
    private final Map<Object, FunctionType> expandedFuncs = new HashMap<>();

    public FunctionSig(TypeSystem ts, List<DType> params, DType ret) {
        this.ts = ts;
        this.typeParams = Collections.emptyList();
        this.params = params;
        this.ret = ret;
        this.dexSig = null;
    }

    public FunctionSig(TypeSystem ts, DexSig sig) {
        this(ts, null, sig);
    }

    public FunctionSig(TypeSystem ts, DType objectType, DexSig dexSig) {
        this.ts = ts;
        this.dexSig = dexSig;
        typeParams = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexTypeParam typeParam : dexSig.typeParams()) {
            DType constraint = ResolveType.$(ts, null, typeParam.paramType());
            PlaceholderType placeholder = new PlaceholderType(ts, typeParam.paramName().toString(), constraint);
            localTypeTable.define(placeholder);
            typeParams.add(placeholder);
        }
        params = new ArrayList<>();
        if (objectType != null) {
            params.add(objectType);
        }
        for (DexParam param : dexSig.params()) {
            params.add(ResolveType.$(ts, localTypeTable, param.paramType()));
        }
        ret = ResolveType.$(ts, localTypeTable, dexSig.ret());
    }

    public void reparent(FunctionType functionType) {
        this.func = functionType;
    }

    public List<DType> params() {
        return params;
    }

    public DType ret() {
        return ret;
    }

    public List<PlaceholderType> typeParams() {
        return typeParams;
    }

    Invoked invoke(Invocation ivc) {
        List<DType> args = ivc.args();
        List<DType> typeArgs = ivc.typeArgs();
        DType retHint = ivc.retHint();
        if (params.size() != args.size()) {
            return new ArgumentsCountIncompatible();
        }
        if (!typeArgs.isEmpty() && typeParams.size() != typeArgs.size()) {
            return new TypeArgumentsCountIncompatible();
        }
        Map<DType, DType> sub = initSub(typeArgs);
        IsAssignable ctx = new IsAssignable(sub);
        boolean needRuntimeCheck = false;
        for (int i = 0; i < params.size(); i++) {
            DType param = params.get(i);
            DType arg = args.get(i);
            IsAssignable paramArg = new IsAssignable(ctx, "#" + i + " param=arg", param, arg);
            IsAssignable argParam = null;
            boolean argMatched = paramArg.result();
            if (!argMatched) {
                needRuntimeCheck = true;
                argParam = new IsAssignable(ctx, "#" + i + " arg=param", arg, param);
                argMatched = argParam.result();
            }
            if (!argMatched) {
                return new ArgumentIncompatible(i, paramArg, argParam);
            }
        }
        if (typeParams.isEmpty()) {
            return new Compatible(needRuntimeCheck, func);
        }
        inferRetHint(retHint, ctx);
        for (PlaceholderType typeParam : typeParams) {
            if (!sub.containsKey(typeParam)) {
                return new MissingTypeArgument(typeParam);
            }
        }
        return new Compatible(needRuntimeCheck, expand(sub));
    }

    @NotNull
    private FunctionType expand(Map<DType, DType> sub) {
        FunctionType expanded = expandedFuncs.get(sub);
        if (expanded != null) {
            return expanded;
        }
        TypeTable localTypeTable = new TypeTable(ts);
        for (Map.Entry<DType, DType> entry : sub.entrySet()) {
            DType key = entry.getKey();
            if (key instanceof NamedType) {
                localTypeTable.define(((NamedType) key).name(), entry.getValue());
            }
        }
        ArrayList<DType> expandedParams = new ArrayList<>();
        for (DexParam param : dexSig.params()) {
            expandedParams.add(ResolveType.$(ts, localTypeTable, param.paramType()));
        }
        DType expandedRet = ResolveType.$(ts, localTypeTable, dexSig.ret());
        expanded = new FunctionType(ts, func.name(), expandedParams, expandedRet);
        expanded.setImplProvider(func.implProvider());
        expandedFuncs.put(sub, expanded);
        return expanded;
    }

    @NotNull
    private Map<DType, DType> initSub(List<DType> typeArgs) {
        Map<DType, DType> collector = new HashMap<>();
        if (typeParams.size() != typeArgs.size()) {
            return collector;
        }
        for (int i = 0; i < typeParams.size(); i++) {
            PlaceholderType typeParam = typeParams.get(i);
            DType typeArg = typeArgs.get(i);
            collector.put(typeParam, typeArg);
        }
        return collector;
    }

    private void inferRetHint(DType retHint, IsAssignable ctx) {
        if (retHint != null) {
            boolean assignable = new IsAssignable(ctx, "ret", retHint, ret).result();
            if (!assignable) {
                new IsAssignable(ctx, "ret", ret, retHint);
            }
        }
    }

    @Override
    public String toString() {
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
        for (DType param : params) {
            if (isFirst) {
                isFirst = false;
            } else {
                desc.append(", ");
            }
            desc.append(param.toString());
        }
        desc.append("): ");
        desc.append(ret.toString());
        return desc.toString();
    }
}
