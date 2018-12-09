package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Int64Type;

import java.util.List;

public class CheckInt64Type implements CheckType.Handler<Int64Type> {

    @Override
    public String handle(Gen g, Int64Type type, List<Class> javaClasses, List<Class> javaInterfaces) {
        g.__(new Line("public static boolean is__int64(Object obj) {"));
        g.__(new Indent(() -> {
            g.__("return obj instanceof Long;");
        }));
        g.__(new Line("}"));
        return "is__int64";
    }
}
