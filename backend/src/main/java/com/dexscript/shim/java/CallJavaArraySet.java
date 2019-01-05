package com.dexscript.shim.java;

import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.type.core.FunctionType;

public class CallJavaArraySet extends FunctionImpl {

    private final Class clazz;

    public CallJavaArraySet(OutShim oShim, FunctionType functionType, Class clazz) {
        super(oShim, functionType);
        this.clazz = clazz;
    }

    @Override
    protected String genCallF() {
        String callF = oShim.allocateShim("array_set");
        Gen g = oShim.g();
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("(("
            ).__(clazz.getCanonicalName()
            ).__(")arg0)[(Integer)arg1] = ("
            ).__(clazz.getComponentType().getCanonicalName()
            ).__(new Line(")arg2;"));
            g.__(new Line("return new ImmediateResult(null);"));
        }));
        g.__(new Line("}"));
        return callF;
    }
}
