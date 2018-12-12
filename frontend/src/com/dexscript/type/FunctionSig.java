package com.dexscript.type;

import com.dexscript.ast.type.DexType;

import java.util.HashMap;
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
        HashMap<NamedType, Type> collector = new HashMap<>();
        Substituted substituted = new Substituted(collector);
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            boolean argMatched = arg.isAssignableFrom(substituted, param) || param.isAssignableFrom(substituted, arg);
            if (!argMatched) {
                return BuiltinTypes.UNDEFINED;
            }
        }
        TypeTable localTypeTable = new TypeTable(this.typeTable);
        for (Map.Entry<NamedType, Type> entry : collector.entrySet()) {
            localTypeTable.define(entry.getKey().name(), entry.getValue());
        }
        return ResolveType.$(localTypeTable, retElem);
    }
}
