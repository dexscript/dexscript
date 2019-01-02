package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
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
        DexInterface arrayInf = new DexInterface(new Text("" +
                "interface Array {\n" +
                "   <T>: interface{}\n" +
                "   Set__(index: int32, element: T)\n" +
                "   Get__(index: int32): T\n" +
                "}"));
        arrayInf.attach(inf.pkg());
        ts.defineInterface(arrayInf);
        ts.lazyDefineFunctions(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        functions.add(newArrayFunc());
        for (DexInfMethod infMethod : inf.methods()) {
            String name = infMethod.identifier().toString();
            FunctionType functionType = new FunctionType(ts, name, null, infMethod.sig());
            functionType.isGlobalSPI(true);
            functions.add(functionType);
        }
        return functions;
    }

    private FunctionType newArrayFunc() {
        DexSig sig = new DexSig(new Text("(<T>: interface{}, class: 'Array', length: int32): Array<T>"));
        sig.attach(inf.pkg());
        FunctionType functionType = new FunctionType(ts, "New__", null, sig);
        functionType.isGlobalSPI(true);
        return functionType;
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
