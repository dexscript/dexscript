package com.dexscript.transpile.skeleton;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.type.DexType;
import com.dexscript.ast.type.DexVoidType;
import com.dexscript.transpile.body.Translate;
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

    public OutInitMethod(OutClass oClass, DexActor iFunc) {
        this.oClass = oClass;
        ts = oClass.typeSystem();
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(iFunc.actorName());
        DeclareParams.$(g, ts, iFunc.sig());
        g.__(" {"
        ).__(new Indent(() -> {
            g.__(new Line("super(scheduler);"));
            genBody(iFunc.params(), iFunc.ret(), iFunc.stmts());
        }));
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
        ).__(new Indent(() -> {
            g.__(new Line("super(scheduler);"));
            OutField thisTask = oClass.allocateField(
                    "TaskOf" + iAwaitConsumer.identifier().toString(),
                    ts.resolveType("Task"));
            iAwaitConsumer.attach(thisTask);
            g.__(thisTask.value()
            ).__(new Line(" = this;"));
            genBody(iAwaitConsumer.params(), iAwaitConsumer.ret(), iAwaitConsumer.stmts());
        }));
    }

    private void genBody(List<DexParam> params, DexType ret, List<DexStatement> stmts) {
        for (DexParam param : params) {
            OutField oField = oClass.allocateField(param.paramName().toString(), ts.resolveType(param.paramType()));
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
        if (ret instanceof DexVoidType) {
            oClass.g().__("produce(null"
            ).__(new Line(");"));
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
