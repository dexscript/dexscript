package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.FunctionType;

import java.lang.reflect.Constructor;

class NewJavaClass extends FunctionImpl {

    private final Constructor ctor;

    public NewJavaClass(OutShim oShim, FunctionType functionType, Constructor ctor) {
        super(oShim, functionType);
        this.ctor = ctor;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String callF = oShim.allocateShim("new__" + ctor.getDeclaringClass().getSimpleName());
        g.__("public static Object "
        ).__(callF);
        DeclareParams.$(g, functionType.params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("return new "
            ).__(ctor.getDeclaringClass().getCanonicalName()
            ).__('(');
            for (int i = 0; i < ctor.getParameterCount(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                g.__('('
                ).__(ctor.getParameterTypes()[i].getCanonicalName()
                ).__(")arg"
                ).__(i + 1);
            }
            g.__(");");
        }));
        g.__(new Line("}"));
        return callF;
    }
}
