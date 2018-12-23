package com.dexscript.shim;

import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;

public class GeneratedSubClass {

    private final Class superClass;
    private final String subClassName;

    GeneratedSubClass(Class superClass, String subClassName) {
        this.superClass = superClass;
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
        ).__(superClass.isInterface() ? " implements "  : " extends "
        ).__(superClass.getCanonicalName()
        ).__(" {"
        ).__(new Indent(() -> {

        })).__(new Line("}"));
        return g.toString();
    }
}
