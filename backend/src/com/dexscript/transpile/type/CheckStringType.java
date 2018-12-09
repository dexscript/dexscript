package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.StringType;

import java.util.List;

public class CheckStringType implements CheckType.Handler<StringType> {

    @Override
    public String handle(Gen g, StringType type, List<Class> javaClasses, List<Class> javaInterfaces) {
        g.__(new Line("public static boolean is__string(Object obj) {"));
        g.__(new Indent(() -> {
            g.__("return obj instanceof String;");
        }));
        g.__(new Line("}"));
        return "is__string";
    }
}
