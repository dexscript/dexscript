package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;

import java.util.HashMap;
import java.util.Map;

public interface InferType {

    Map<Class<? extends DexElement>, InferType> inferTypes = new HashMap<>() {{
        put(DexStringLiteral.class, elem -> BuiltinTypes.STRING);
    }};

    Type infer(DexElement elem);

    static Type inferType(DexElement elem) {
        InferType inferType = inferTypes.get(elem.getClass());
        if (inferType == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return inferType.infer(elem);
    }
}
