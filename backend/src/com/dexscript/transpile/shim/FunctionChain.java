package com.dexscript.transpile.shim;

import com.dexscript.transpile.gen.*;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.FunctionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionChain {

    private final String funcName;
    private final int paramsCount;
    private final List<FunctionType> functions;

    public FunctionChain(String funcName, int paramsCount, List<FunctionType.Invoked> invokeds) {
        this.funcName = funcName;
        this.paramsCount = paramsCount;
        this.functions = new ArrayList<>();
        for (FunctionType.Invoked invoked : invokeds) {
            functions.add(invoked.function());
        }
    }

    public void gen(Gen g, String chainF) {
        g.__("public static Promise "
        ).__(chainF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : functions) {
                if (!(funcType.attachment() instanceof FunctionImpl)) {
                    throw new IllegalStateException("no implementation attached to function: " + funcType);
                }
                FunctionImpl implEntry = (FunctionImpl) funcType.attachment();
                g.__("if ("
                ).__(implEntry.canF());
                InvokeParams.$(g, paramsCount, false);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    if (implEntry.newF() == null) {
                        g.__("return "
                        ).__(implEntry.callF());
                        InvokeParams.$(g, paramsCount, false);
                        g.__(new Line(";"));
                    } else {
                        g.__("return "
                        ).__(implEntry.newF());
                        InvokeParams.$(g, paramsCount, true);
                        g.__(new Line(";"));
                    }
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
