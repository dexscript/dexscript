package com.dexscript.transpile.shim.impl;

import com.dexscript.ast.DexActor;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.FunctionImpl;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

public class CallActor extends FunctionImpl {

    private final DexActor function;
    private final boolean hasAwait;

    public CallActor(FunctionType functionType, DexActor function, String canF, String newF, boolean hasAwait) {
        super(functionType, canF, null, newF);
        this.function = function;
        this.hasAwait = hasAwait;
    }

    public DexActor function() {
        return function;
    }

    @Override
    public boolean hasAwait() {
        return hasAwait;
    }

    protected void genNewF(Gen g) {
        String newF = OutShim.stripPrefix(newF());
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, functionType().params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            String className = OutTopLevelClass.qualifiedClassNameOf(function);
            g.__("return new "
            ).__(className
            ).__("(scheduler");
            for (int i = 0; i < functionType().params().size(); i++) {
                g.__(", ");
                Type paramType = functionType().params().get(i);
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
    }
}
