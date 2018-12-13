package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.*;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

import java.lang.reflect.Method;

public class CallJavaFunction extends FunctionImpl {

    private final Method javaFunction;

    public CallJavaFunction(OutShim oShim, FunctionType functionType, Method javaFunction) {
        super(oShim, functionType);
        this.javaFunction = javaFunction;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String callF = oShim.allocateShim("call__" + javaFunction.getName());
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult("
            ).__(javaFunction.getDeclaringClass().getCanonicalName()
            ).__('.'
            ).__(javaFunction.getName()
            ).__('(');
            for (int i = 0; i < functionType.params().size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                Type paramType = functionType.params().get(i);
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i + 1
                ).__(')');
            }
            g.__("));");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
