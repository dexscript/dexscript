package com.dexscript.shim;

import com.dexscript.gen.*;
import com.dexscript.shim.gen.DeclareParams;
import com.dexscript.shim.gen.InvokeParams;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.shim.java.JavaTypes;
import com.dexscript.type.core.FunctionSig;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.Dispatched;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionChain {

    private final String funcName;
    private final int paramsCount;
    private final List<FunctionType> functions;

    public FunctionChain(String funcName, int paramsCount, Dispatched invoked) {
        this.funcName = funcName;
        this.paramsCount = paramsCount;
        this.functions = new ArrayList<>();
        for (FunctionSig.Invoked candidate : invoked.candidates) {
            functions.add(candidate.func());
        }
    }

    public void gen(Gen g, String chainF, JavaTypes javaTypes) {
        for (FunctionType funcType : functions) {
            if (!(funcType.impl() instanceof FunctionImpl)) {
                throw new IllegalStateException("no implementation attached to function: " + funcType);
            }
            FunctionImpl impl = (FunctionImpl) funcType.impl();
            impl.canF(javaTypes);
            impl.callF();
        }
        g.__("public static Object "
        ).__(chainF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : functions) {
                FunctionImpl impl = (FunctionImpl) funcType.impl();
                g.__("if ("
                ).__(impl.canF(javaTypes));
                InvokeParams.$(g, paramsCount, false);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                        g.__("return "
                        ).__(impl.callF());
                        InvokeParams.$(g, paramsCount, true);
                        g.__(new Line(";"));
                }));
                g.__(new Line("}"));
            }
            g.__("throw new DexRuntimeException(\"no implementation of "
            ).__(funcName
            ).__(" accepted the invocation: \" + java.util.Arrays.asList");
            InvokeParams.$(g, paramsCount, false);
            g.__(new Line(");"));
        }));
        g.__(new Line("}"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionChain that = (FunctionChain) o;
        return paramsCount == that.paramsCount &&
                Objects.equals(funcName, that.funcName) &&
                Objects.equals(functions, that.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, paramsCount, functions);
    }
}
