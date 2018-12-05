package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Type;

import java.util.List;

public class TranslateInt64Type implements TranslateType {
    @Override
    public String handle(Gen g, Type type, List<Class> javaClasses, List<Class> javaInterfaces) {
        g.__(new Line("public static boolean is__int64(Object obj) {"));
        g.__(new Indent(() -> {
            g.__("return obj instanceof Long;");
        }));
        g.__(new Line("}"));
        return "is__int64";
    }
}
