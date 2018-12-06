package com.dexscript.transpile.method;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.ast.func.DexStatement;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutField;
import com.dexscript.transpile.OutInnerClass;
import com.dexscript.transpile.elem.Translate;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.TypeSystem;

import java.util.List;

public class OutInitMethod implements OutMethod {

    private final OutClass oClass;
    private final Gen g;
    private final TypeSystem ts;

    public OutInitMethod(OutClass oClass, DexFunction iFunc) {
        this.oClass = oClass;
        ts = oClass.typeSystem();
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(iFunc.actorName());
        DeclareParams.$(g, ts, iFunc.sig());
        g.__(" {"
        ).__(new Indent(() -> genBody(iFunc.params(), iFunc.stmts())));
    }

    public OutInitMethod(OutInnerClass oClass, DexAwaitConsumer iAwaitConsumer) {
        this.oClass = oClass;
        ts = oClass.typeSystem();
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(iAwaitConsumer.identifier().toString());
        DeclareParams.$(g, ts, iAwaitConsumer.produceSig());
        g.__(" {"
        ).__(new Indent(() -> genBody(iAwaitConsumer.params(), iAwaitConsumer.stmts())));
    }

    private void genBody(List<DexParam> params, List<DexStatement> stmts) {
        for (DexParam param : params) {
            OutField oField = oClass.allocateField(param.paramName().toString(), InferType.$(ts, param.paramType()));
            param.attach(oField);
            g.__("this."
            ).__(oField.value()
            ).__(" = "
            ).__(param.paramName().toString()
            ).__(new Line(";"));
        }
        for (DexStatement stmt : stmts) {
            Translate.$(oClass, stmt);
        }
    }

    public TypeSystem typeSystem() {
        return ts;
    }

    public OutClass oClass() {
        return oClass;
    }

    public Gen g() { return g; }

    @Override
    public String finish() {
        g.indention(oClass.indention());
        g.__(new Line(""));
        g.__(new Line("}"));
        return g.toString();
    }
}
