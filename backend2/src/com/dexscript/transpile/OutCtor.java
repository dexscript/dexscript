package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.func.DexStatement;
import com.dexscript.transpile.elem.Translate;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.TypeSystem;

public class OutCtor implements OutMethod {

    private final OutClass oClass;
    private final DexFunction iFunc;
    private final Gen g;
    private final TypeSystem ts;

    public OutCtor(OutClass oClass, DexFunction iFunc) {
        this.oClass = oClass;
        this.iFunc = iFunc;
        ts = oClass.typeSystem();
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(className()
        ).__(new OutSig(ts, iFunc.sig(),true).toString()
        ).__(" {"
        ).__(new Indent(this::genBody)
        ).__(new Line("}"));
    }

    private void genBody() {
        for (DexParam param : iFunc.sig().params()) {
            OutField oField = oClass.allocateField(param.paramName().toString(), ts.resolveType(param.paramType()));
            param.attach(oField);
            g.__("this."
            ).__(oField.value()
            ).__(" = "
            ).__(param.paramName().toString()
            ).__(new Line(";"));
        }
        for (DexStatement stmt : iFunc.stmts()) {
            Translate.$(oClass, stmt);
        }
    }

    public String className() {
        return iFunc.identifier().toString();
    }

    @Override
    public String toString() {
        return g.toString();
    }

    public TypeSystem typeSystem() {
        return ts;
    }

    public OutClass oClass() {
        return oClass;
    }

    public DexFunction iFunc() {
        return iFunc;
    }

    public Gen g() { return g; }
}
