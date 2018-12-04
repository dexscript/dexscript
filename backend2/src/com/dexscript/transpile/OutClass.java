package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.core.DexElement;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class OutClass {

    private final TypeSystem ts;
    private final DexFunction iFunc;
    private final Gen g = new Gen();
    private final OutFields oFields = new OutFields();
    private OutMethod oMethod;

    public OutClass(TypeSystem ts, DexFunction iFunc) {
        this.ts = ts;
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
//        g.__(new Line("import com.dexscript.runtime.gen__.*;"));
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
            ).__(oField.type().javaClassName()
            ).__(' '
            ).__(oField.value()
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

    public TypeSystem typeSystem() {
        return ts;
    }

    public DexFunction iFunc() {
        return iFunc;
    }

    public Gen g() {
        return oMethod.g();
    }

    public void changeMethod(OutMethod oMethod) {
        this.oMethod = oMethod;
    }
}
