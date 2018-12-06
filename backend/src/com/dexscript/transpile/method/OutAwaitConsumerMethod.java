package com.dexscript.transpile.method;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.ast.func.DexStatement;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutField;
import com.dexscript.transpile.elem.Translate;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.TypeSystem;

public class OutAwaitConsumerMethod implements OutMethod {

    private final Gen g;
    private final OutClass oClass;
    private final DexAwaitConsumer awaitConsumer;
    private final TypeSystem ts;

    public OutAwaitConsumerMethod(OutClass oClass, DexAwaitConsumer awaitConsumer) {
        oClass.changeMethod(this);
        this.oClass = oClass;
        this.awaitConsumer = awaitConsumer;
        ts = oClass.typeSystem();
        g = new Gen(oClass.indention());
        g.__("public Result "
        ).__(awaitConsumer.identifier().toString());
        DeclareParams.$(g, ts, awaitConsumer.produceSig());
        g.__(" {"
        ).__(new Indent(this::genBody));
    }

    private void genBody() {
        for (DexParam param : awaitConsumer.params()) {
            OutField oField = oClass.allocateField(param.paramName().toString(), InferType.$(ts, param.paramType()));
            param.attach(oField);
            g.__("this."
            ).__(oField.value()
            ).__(" = "
            ).__(param.paramName().toString()
            ).__(new Line(";"));
        }
        for (DexStatement stmt : awaitConsumer.stmts()) {
            Translate.$(oClass, stmt);
        }
    }

    public OutClass oClass() {
        return oClass;
    }

    @Override
    public Gen g() {
        return g;
    }

    @Override
    public String finish() {
        g.__(new Line("}"));
        return g.toString();
    }
}
