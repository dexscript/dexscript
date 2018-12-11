package com.dexscript.type;

import com.dexscript.ast.type.DexGenericExpansionType;
import com.dexscript.ast.type.DexType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

// part of FunctionType
class FunctionSig {

    private final TypeTable typeTable;
    private final List<PlaceholderType> typeParams;
    private final List<Type> params;
    private final Type ret;
    private final DexType retElem;

    FunctionSig(TypeTable typeTable, List<PlaceholderType> typeParams, List<Type> params, Type ret, DexType retElem) {
        this.typeTable = typeTable;
        this.typeParams = typeParams;
        this.params = params;
        this.ret = ret;
        this.retElem = retElem;
    }

    Type invoke(List<Type> typeArgs, List<Type> args, Type retHint) {
        if (params.size() != args.size()) {
            return BuiltinTypes.UNDEFINED;
        }
        Subs subs = new Subs();
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            boolean argMatched = arg.isAssignableFrom(subs, param) || param.isAssignableFrom(subs, arg);
            if (!argMatched) {
                return BuiltinTypes.UNDEFINED;
            }
        }
        Map<PlaceholderType, Type> deduced = subs.deduce();
        TypeTable localTypeTable = new TypeTable(this.typeTable);
        for (Map.Entry<PlaceholderType, Type> entry : deduced.entrySet()) {
            localTypeTable.define(entry.getKey().name(), entry.getValue());
        }
        return ResolveType.$(localTypeTable, retElem);
    }
}
