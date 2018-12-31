package com.dexscript.shim.java;

import com.dexscript.gen.*;
import com.dexscript.shim.OutShim;
import com.dexscript.type.FunctionType;

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
        DeclareParams.$(g, functionType.params().size() + 1, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult("
            ).__(javaFunction.getDeclaringClass().getCanonicalName()
            ).__('.'
            ).__(javaFunction.getName()
            ).__('(');
            Class<?>[] paramTypes = javaFunction.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) {
                    g.__(", ");
                }
                Class<?> paramType = paramTypes[i];
                g.__("(("
                ).__(paramType.getCanonicalName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__("));");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
