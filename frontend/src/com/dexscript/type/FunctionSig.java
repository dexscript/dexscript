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

        public abstract DType ret();

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
        public DType ret() {
            return ts.UNDEFINED;
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
        private final DType ret;

        public Compatible(boolean needRuntimeCheck, DType ret) {
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
        public DType ret() {
            return ret;
        }
    }


    private final TypeSystem ts;
    private FunctionType functionType;
    private final List<PlaceholderType> typeParams;
    private final List<DType> params;
    private final DType ret;
    private final DexType retElem;

    public FunctionSig(TypeSystem ts, List<DType> params, DType ret) {
        this.ts = ts;
        this.typeParams = Collections.emptyList();
        this.params = params;
        this.ret = ret;
        this.retElem = null;
    }

    public FunctionSig(TypeSystem ts, List<PlaceholderType> typeParams, List<DType> params, DType ret, DexType retElem) {
        this.ts = ts;
        this.typeParams = typeParams == null ? Collections.emptyList() : typeParams;
        this.params = params;
        this.ret = ret;
        this.retElem = retElem;
    }

    public FunctionSig(TypeSystem ts, DexSig sig) {
        this(ts, null, sig);
    }

    public FunctionSig(TypeSystem ts, DType objectType, DexSig sig) {
        this.ts = ts;
        typeParams = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexTypeParam typeParam : sig.typeParams()) {
            DType constraint = ResolveType.$(ts, null, typeParam.paramType());
            PlaceholderType placeholder = new PlaceholderType(ts, typeParam.paramName().toString(), constraint);
            localTypeTable.define(placeholder);
            typeParams.add(placeholder);
        }
        params = new ArrayList<>();
        if (objectType != null) {
            params.add(objectType);
        }
        for (DexParam param : sig.params()) {
            params.add(ResolveType.$(ts, localTypeTable, param.paramType()));
        }
        ret = ResolveType.$(ts, localTypeTable, sig.ret());
        retElem = sig.ret();
    }

    public void reparent(FunctionType functionType) {
        this.functionType = functionType;
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
        Map<DType, DType> sub = initSub(typeArgs);
        TypeComparisonContext ctx = new TypeComparisonContext(sub);
        boolean needRuntimeCheck = false;
        for (int i = 0; i < params.size(); i++) {
            DType param = params.get(i);
            DType arg = args.get(i);
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
        TypeTable localTypeTable = new TypeTable(ts);
        for (Map.Entry<DType, DType> entry : sub.entrySet()) {
            DType key = entry.getKey();
            if (key instanceof NamedType) {
                localTypeTable.define(((NamedType) key).name(), entry.getValue());
            }
        }
        DType expandedRet = ResolveType.$(ts, localTypeTable, retElem);
        return new Compatible(needRuntimeCheck, expandedRet);
    }

    @NotNull
    private Map<DType, DType> initSub(List<DType> typeArgs) {
        Map<DType, DType> collector = new HashMap<>();
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
            DType typeArg = typeArgs.get(i);
            collector.put(typeParam, typeArg);
        }
        return collector;
    }

    private void inferRetHint(DType retHint, TypeComparisonContext ctx) {
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
