package com.dexscript.transpile.shim.impl;

import com.dexscript.ast.DexFunction;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

public class NewActor extends Impl {

    private final DexFunction function;

    public NewActor(FunctionType functionType, DexFunction function, String canF, String newF) {
        super(functionType, canF, null, newF);
        this.function = function;
    }

    public DexFunction function() {
        return function;
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
            for (int i = 1; i < functionType().params().size(); i++) {
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
