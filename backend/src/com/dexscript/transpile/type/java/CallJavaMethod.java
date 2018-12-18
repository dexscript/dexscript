package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;

import java.lang.reflect.Method;

class CallJavaMethod extends FunctionImpl {

    private final Method javaMethod;

    public CallJavaMethod(OutShim oShim, FunctionType functionType, Method javaMethod) {
        super(oShim, functionType);
        this.javaMethod = javaMethod;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String callF = oShim.allocateShim("call__" + javaMethod.getName());
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult((("
            ).__(javaMethod.getDeclaringClass().getCanonicalName()
            ).__(")arg1)."
            ).__(javaMethod.getName()
            ).__('(');
            Class<?>[] paramTypes = javaMethod.getParameterTypes();
            for (int i = 1; i < paramTypes.length; i++) {
                if (i > 1) {
                    g.__(", ");
                }
                Class<?> paramType = paramTypes[i];
                g.__("(("
                ).__(paramType.getCanonicalName()
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
