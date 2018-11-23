package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

public class OutClass {

    private final DexFunction iFunc;
    private final Gen g = new Gen();

    public OutClass(Township township, DexFunction iFunc) {
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public class "
        ).__(className()
        ).__(" extends Actor {"
        ).__(new Indent(() -> {
            g.__(new OutCtor(township, g.prefix(), iFunc).toString());
        }));
        g.__(new Line("}"));
    }

    public String packageName() {
        return iFunc.file().packageClause().identifier().toString();
    }

    public String className() {
        return iFunc.identifier().toString();
    }

    public String qualifiedClassName() {
        return packageName() + "." + className();
    }

    @Override
    public String toString() {
        return g.toString();
    }
}
