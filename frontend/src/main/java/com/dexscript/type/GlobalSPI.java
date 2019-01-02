package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.inf.DexInfMethod;

import java.util.ArrayList;
import java.util.List;

public class GlobalSPI implements FunctionsType {

    private TypeSystem ts;
    private DexInterface inf;
    private List<FunctionType> functions;

    public GlobalSPI(TypeSystem ts, DexInterface inf) {
        this.ts = ts;
        this.inf = inf;
        ts.lazyDefineFunctions(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        for (DexInfMethod infMethod : inf.methods()) {
            String name = infMethod.identifier().toString();
            FunctionType functionType = new FunctionType(ts, name, null, infMethod.sig());
            functionType.isGlobalSPI(true);
            functions.add(functionType);
        }
        return functions;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
