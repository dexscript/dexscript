package com.dexscript.shim.java;

import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.type.FunctionType;

public class CallJavaArraySet extends FunctionImpl {

    private final Class clazz;

    public CallJavaArraySet(OutShim oShim, FunctionType functionType, Class clazz) {
        super(oShim, functionType);
        this.clazz = clazz;
    }

    @Override
    protected String genCallF() {
        String callF = oShim.allocateShim("array_get");
        Gen g = oShim.g();
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult((("
            ).__(clazz.getCanonicalName()
            ).__(")arg0)[(Integer)arg1]);");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
