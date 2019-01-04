package com.dexscript.type;

import com.dexscript.ast.DexPackage;
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

        public FunctionType func() {
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
        public FunctionType func() {
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
    private final DexPackage pkg;
    private FunctionType func;
    private final List<PlaceholderType> typeParams;
    private final List<FunctionParam> params;
    private DType contextParam;
    private final DType ret;
    private final DexSig dexSig;
    private final Map<Object, FunctionType> expandedFuncs = new HashMap<>();

    public FunctionSig(TypeSystem ts, DexPackage pkg, List<FunctionParam> params, DType ret) {
        this.ts = ts;
        this.typeParams = Collections.emptyList();
        this.params = params;
        this.pkg = pkg;
        this.ret = ret;
        this.dexSig = null;
    }

    public FunctionSig(TypeSystem ts, TypeTable localTypeTable, DexSig dexSig) {
        this(ts, localTypeTable, null, dexSig);
    }

    public FunctionSig(TypeSystem ts, TypeTable localTypeTable, DType self, DexSig dexSig) {
        this.ts = ts;
        this.pkg = dexSig.pkg();
        this.dexSig = dexSig;
        typeParams = new ArrayList<>();
        localTypeTable = new TypeTable(ts, localTypeTable);
        for (DexTypeParam typeParam : dexSig.typeParams()) {
            DType constraint = ResolveType.$(ts, null, typeParam.paramType());
            String paramName = typeParam.paramName().toString();
            if (localTypeTable.resolveType(pkg, paramName) != ts.UNDEFINED) {
                continue;
            }
            PlaceholderType paramType = new PlaceholderType(ts, paramName, constraint);
            localTypeTable.define(pkg, paramName, paramType);
            typeParams.add(paramType);
        }
        params = new ArrayList<>();
        if (self != null) {
            params.add(new FunctionParam("self", self));
        }
        for (DexParam param : dexSig.params()) {
            String name = param.paramName().toString();
            DType type = ResolveType.$(ts, localTypeTable, param.paramType());
            params.add(new FunctionParam(name, type));
        }
        contextParam = ts.context(dexSig.pkg());
        ret = ResolveType.$(ts, localTypeTable, dexSig.ret());
    }

    public void reparent(FunctionType functionType) {
        this.func = functionType;
    }

    public List<FunctionParam> params() {
        return params;
    }

    public DType contextParam() {
        if (contextParam != null) {
            return contextParam;
        }
        contextParam = ts.context(pkg);
        return contextParam;
    }

    public DType ret() {
        return ret;
    }

    public List<PlaceholderType> typeParams() {
        return typeParams;
    }

    Invoked invoke(List<DType> typeArgs, List<DType> args, DType retHint) {
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
            FunctionParam param = params.get(i);
            DType arg = args.get(i);
            IsAssignable paramArg = new IsAssignable(ctx, "#" + i + " param=arg", param.type(), arg);
            IsAssignable argParam = null;
            boolean argMatched = paramArg.result();
            if (!argMatched) {
                needRuntimeCheck = true;
                argParam = new IsAssignable(ctx, "#" + i + " arg=param", arg, param.type());
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
            if (key instanceof PlaceholderType) {
                localTypeTable.define(dexSig.pkg(), ((PlaceholderType)key).name(), entry.getValue());
            }
        }
        expanded = new FunctionType(ts, func.name(), localTypeTable, dexSig);
        expanded.implProvider(func.implProvider());
        expanded.isGlobalSPI(func.isGlobalSPI());
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
        for (FunctionParam param : params) {
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
