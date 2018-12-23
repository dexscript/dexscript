package com.dexscript.pkg;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.type.*;

import java.util.ArrayList;
import java.util.List;

interface SPI {

    static void define(TypeSystem ts, DexInterface inf) {
        if ("::".equals(inf.identifier().toString())) {
            defineGlobalSPI(ts, inf);
            return;
        }
        ts.defineInterface(inf);
    }

    static void defineGlobalSPI(TypeSystem ts, DexInterface inf) {
        for (DexInfMethod infMethod : inf.methods()) {
            String name = infMethod.identifier().toString();
            List<FunctionParam> params = new ArrayList<>();
            for (DexParam param : infMethod.sig().params()) {
                String paramName = param.paramName().toString();
                DType paramType = ResolveType.$(ts, null, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            DType ret = ResolveType.$(ts, null, infMethod.sig().ret());
            ts.defineFunction(new FunctionType(ts, name, params, ret));
        }
    }
}
