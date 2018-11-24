package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.resolve.Denotation;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

public class OutCtor {

    private final OutClass oClass;
    private final DexFunction iFunc;
    private final Gen g;
    private Township township;

    public OutCtor(OutClass oClass, DexFunction iFunc) {
        township = oClass.township();
        this.oClass = oClass;
        this.iFunc = iFunc;
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(className()
        ).__(new OutSig(township, iFunc.sig()).toString()
        ).__(" {"
        ).__(new Indent(this::genBody)
        ).__(new Line("}"));
    }

    private void genBody() {
        for (DexParam param : iFunc.sig().params()) {
            OutField oField = oClass.allocateField(param.paramName(), township.resolveType(param.paramType()));
            param.attach(oField);
            g.__("this."
            ).__(oField.fieldName
            ).__(" = "
            ).__(param.paramName().toString()
            ).__(new Line(";"));
        }
        for (DexStatement stmt : iFunc.block().stmts()) {
            if (stmt instanceof DexReturnStmt) {
                genStmt((DexReturnStmt) stmt);
                return;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void genStmt(DexReturnStmt returnStmt) {
        Denotation.Type retType = township.resolveType(iFunc.sig().ret());
        OutExpr oExpr = new OutExpr(this, returnStmt.expr());
        g.__("finish(("
        ).__(retType.javaClassName
        ).__(')'
        ).__(oExpr.value()
        ).__(new Line(");"));
    }

    public String className() {
        return iFunc.identifier().toString();
    }

    @Override
    public String toString() {
        return g.toString();
    }

    public String indention() {
        return g.indention();
    }

    public Township township() {
        return township;
    }
}
