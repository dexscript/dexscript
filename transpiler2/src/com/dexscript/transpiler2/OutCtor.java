package com.dexscript.transpiler2;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.transpiler2.gen.Gen;
import com.dexscript.transpiler2.gen.Indent;
import com.dexscript.transpiler2.gen.Line;

public class OutCtor {

    private final DexFunction iFunc;
    private final Gen g;

    public OutCtor(String prefix, DexFunction iFunc) {
        this.iFunc = iFunc;
        g = new Gen(prefix);
        g.__("public "
        ).__(className()
        ).__('('
        ).__(") {"
        ).__(new Indent(this::genBody)
        ).__('}');
    }

    private void genBody() {
        for (DexStatement stmt : iFunc.block().stmts()) {
            if (stmt instanceof DexReturnStmt) {
                genStmt((DexReturnStmt)stmt);
                return;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void genStmt(DexReturnStmt returnStmt) {
        OutExpr oExpr = new OutExpr(g.prefix(), returnStmt.expr());
        g.__("finish("
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
}
