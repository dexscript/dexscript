package com.dexscript.transpile.shim.impl;

import com.dexscript.transpile.gen.*;
import com.dexscript.transpile.shim.FunctionImpl;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

import java.lang.reflect.Method;

public class CallJavaFunction extends FunctionImpl {

    private final Method javaFunction;

    public CallJavaFunction(FunctionType functionType, Method javaFunction, String canF, String callF) {
        super(functionType, canF, callF, null);
        this.javaFunction = javaFunction;
    }

    public final Method javaFunction() {
        return javaFunction;
    }

    @Override
    public boolean hasAwait() {
        return false;
    }

    @Override
    protected void genCallF(Gen g) {
        String callF = OutShim.stripPrefix(callF());
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType().params().size(), false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult("
            ).__(javaFunction.getDeclaringClass().getCanonicalName()
            ).__('.'
            ).__(javaFunction.getName()
            ).__('(');
            for (int i = 0; i < functionType().params().size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                Type paramType = functionType().params().get(i);
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__("));");
        }));
        g.__(new Line("}"));
    }
}
