package com.dexscript.transpile.shim;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

public class GeneratedSubClass {

    private final String superClassName;
    private final String subClassName;

    GeneratedSubClass(String superClassName, String subClassName) {
        this.superClassName = superClassName;
        this.subClassName = subClassName;
    }

    public String qualifiedClassName() {
        return OutShim.PACKAGE_NAME + subClassName;
    }

    public String gen() {
        Gen g = new Gen();
        g.__(new Line("package com.dexscript.runtime.gen;")
        ).__("public class "
        ).__(subClassName
        ).__(" extends "
        ).__(superClassName
        ).__(" {"
        ).__(new Indent(() -> {

        })).__(new Line("}"));
        return g.toString();
    }
}
