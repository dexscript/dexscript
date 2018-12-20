package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;

public class CallJavaArrayGet extends FunctionImpl {

    private final Class clazz;

    public CallJavaArrayGet(OutShim oShim, FunctionType functionType, Class clazz) {
        super(oShim, functionType);
        this.clazz = clazz;
    }

    @Override
    protected String genCallF() {
        String callF = oShim.allocateShim("array_get");
        Gen g = oShim.g();
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult((("
            ).__(clazz.getCanonicalName()
            ).__(")arg1)[(Integer)arg2]);");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
