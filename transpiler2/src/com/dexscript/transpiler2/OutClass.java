package com.dexscript.transpiler2;

import com.dexscript.parser2.DexFunction;
import com.dexscript.transpiler2.gen.Gen;
import com.dexscript.transpiler2.gen.Indent;
import com.dexscript.transpiler2.gen.NL;

public class OutClass {

    private final DexFunction iFunc;
    private final Gen g = new Gen();

    public OutClass(DexFunction iFunc) {
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(';'
        ).__(new NL());
        g.__("public class "
        ).__(className()
        ).__(" {"
        ).__(new Indent(() -> {
            genCtor();
        }));
        g.__('}'
        ).__(new NL());
    }

    private void genCtor() {
        g.__("public "
        ).__(className()
        ).__('('
        ).__(") {"
        ).__(new Indent(() -> {

                })
        ).__('}');
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
