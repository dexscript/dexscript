package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.HashMap;
import java.util.Map;

public interface InferType {

    Map<Class<? extends DexExpr>, InferType> inferTypes = new HashMap<>() {{
        put(DexStringLiteral.class, (ts, elem) -> BuiltinTypes.STRING);
        put(DexReference.class, (ts, elem) -> InferValue.inferValue(ts, (DexReference) elem).type());
    }};

    Type infer(TypeSystem typeSystem, DexExpr elem);

    static Type inferType(TypeSystem ts, DexExpr elem) {
        InferType inferType = inferTypes.get(elem.getClass());
        if (inferType == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return inferType.infer(ts, elem);
    }
}
