package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

class NewJavaClass extends FunctionImpl {

    private final Class clazz;

    public NewJavaClass(OutShim oShim, FunctionType functionType, Class clazz) {
        super(oShim, functionType);
        this.clazz = clazz;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String callF = oShim.allocateShim("new__" + clazz.getSimpleName());
        g.__("public static Promise "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size() + 1, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new ImmediateResult(new "
            ).__(clazz.getCanonicalName()
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
