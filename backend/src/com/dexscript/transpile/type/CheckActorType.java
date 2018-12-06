package com.dexscript.transpile.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.ActorType;

import java.util.List;

public class CheckActorType implements CheckType<ActorType> {

    @Override
    public String handle(Gen g, ActorType actorType, List<Class> javaClasses, List<Class> javaInterfaces) {
        DexFunction elem = actorType.elem();
        String typeCheck = "is__" + elem.actorName();
        g.__("public static boolean "
        ).__(typeCheck
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__("return obj instanceof "
            ).__(OutTopLevelClass.qualifiedClassNameOf(elem)
            ).__(";");
        }));
        g.__(new Line("}"));
        return typeCheck;
    }
}
