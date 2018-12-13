package com.dexscript.transpile.shim.impl;

import com.dexscript.transpile.gen.*;

import java.util.List;
import java.util.Objects;

public class VirtualFunction {

    private final String funcName;
    private final int paramsCount;

    public VirtualFunction(String funcName, int paramsCount) {
        this.funcName = funcName;
        this.paramsCount = paramsCount;
    }

    public void finish(Gen g, List<FunctionImpl> impls) {
        g.__("public static Object "
        ).__(funcName);
        DeclareParams.$(g, paramsCount, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(new Line("Scheduler scheduler = new Scheduler();"));
            for (FunctionImpl impl : impls) {
                if (impl.newF() == null) {
                    continue;
                }
                g.__("if ("
                ).__(impl.canF());
                InvokeParams.$(g, paramsCount, false);
                g.__(") {");
                g.__(new Indent(() -> {
                    g.__("Promise result = "
                    ).__(impl.newF());
                    InvokeParams.$(g, paramsCount, true);
                    g.__(new Line(";"));
                    g.__(new Line("scheduler.schedule();"));
                    g.__(new Line("if (!result.finished()) {"));
                    g.__(new Line("  throw new DexRuntimeException(\"function never return\");"));
                    g.__(new Line("}"));
                    g.__(new Line("return result.value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new DexRuntimeException();"));
        }));
        g.__(new Line("}"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualFunction that = (VirtualFunction) o;
        return paramsCount == that.paramsCount &&
                Objects.equals(funcName, that.funcName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, paramsCount);
    }

    @Override
    public String toString() {
        return "VirtualFunction{" +
                "funcName='" + funcName + '\'' +
                ", paramsCount=" + paramsCount +
                '}';
    }
}
