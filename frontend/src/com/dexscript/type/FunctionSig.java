package com.dexscript.type;

import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.type.DexType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// part of FunctionType
public class FunctionSig {

    public interface OnIncompatibleArgument {
        void handle(FunctionSig sig, List<Type> typeArgs, List<Type> args, Type retHint,
                    int index, Type arg, Type param, Map<Type, Type> sub);
    }

    public static OnIncompatibleArgument ON_INCOMPATIBLE_ARGUMENT =
            (sig, typeArgs, args, retHint, index, arg, param, sub) -> {

            };
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

    Type invoke(TypeTable typeTable, List<Type> typeArgs, List<Type> args, Type retHint) {
        if (params.size() != args.size()) {
            return BuiltinTypes.UNDEFINED;
        }
        Map<Type, Type> sub = initSub(typeArgs);
        TypeComparisonContext ctx = new TypeComparisonContext(sub);
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            TypeComparisonContext subCtx = new TypeComparisonContext(ctx);
            boolean argMatched = arg.isAssignableFrom(subCtx, param);
            if (!argMatched) {
                subCtx.rollback();
                argMatched = param.isAssignableFrom(subCtx, arg);
            }
            if (!argMatched) {
                ON_INCOMPATIBLE_ARGUMENT.handle(this, typeArgs, args, retHint, i, arg, param, sub);
                return BuiltinTypes.UNDEFINED;
            }
            subCtx.commit();
        }
        inferRetHint(retHint, ctx);
        if (retElem == null || typeParams == null) {
            return ret;
        }
        for (PlaceholderType typeParam : typeParams) {
            if (!sub.containsKey(typeParam)) {
                throw new DexSyntaxException("can not infer type parameter: " + typeParam);
            }
        }
        TypeTable localTypeTable = new TypeTable(typeTable);
        for (Map.Entry<Type, Type> entry : sub.entrySet()) {
            Type key = entry.getKey();
            if (key instanceof NamedType) {
                localTypeTable.define(((NamedType) key).name(), entry.getValue());
            }
        }
        return ResolveType.$(localTypeTable, retElem);
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
        if (typeParams.size() != typeArgs.size()) {
            throw new DexSyntaxException("invoke function with wrong type arguments: " +
                    typeArgs + ", expect: " + typeParams);
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
}
