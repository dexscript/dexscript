package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

public class OutClass {

    private final Town town;
    private final DexFunction iFunc;
    private final Gen g = new Gen();
    private final OutFields oFields = new OutFields();

    public OutClass(Town town, DexFunction iFunc) {
        this.town = town;
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__(new Line("import com.dexscript.runtime.gen__.*;"));
        g.__("public class "
        ).__(className()
        ).__(" extends Actor {"
        ).__(new Indent(() -> {
            g.__(new OutCtor(this, iFunc).toString());
            genFields();
        }));
        g.__(new Line("}"));
    }

    public static String qualifiedClassNameOf(DexFunction iFunction) {
        String packageName = iFunction.file().packageClause().identifier().toString();
        return packageName + "." + iFunction.identifier().toString();
    }

    private void genFields() {
        for (OutField oField : oFields) {
            g.__("private "
            ).__(oField.fieldType.javaClassName()
            ).__(' '
            ).__(oField.fieldName
            ).__(new Line(";"));
        }
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

    public OutField allocateField(DexElement iElem, Type type) {
        return oFields.allocate(iElem, type);
    }

    public String indention() {
        return g.indention();
    }

    public Town township() {
        return town;
    }
}
