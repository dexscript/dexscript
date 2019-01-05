package com.dexscript.shim.java;

import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.type.core.FunctionType;

public class NewJavaArray extends FunctionImpl {

    public NewJavaArray(OutShim oShim, FunctionType functionType) {
        super(oShim, functionType);
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String callF = oShim.allocateShim("new__Array");
        g.__("public static Object "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new Object[(Integer)arg1];");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
