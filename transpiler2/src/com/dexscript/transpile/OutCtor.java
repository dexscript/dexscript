package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.ResolveType;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

public class OutCtor {

    private final Township township;
    private final DexFunction iFunc;
    private final Gen g;

    public OutCtor(Township township, String prefix, DexFunction iFunc) {
        this.township = township;
        this.iFunc = iFunc;
        g = new Gen(prefix);
        for (DexParam param : iFunc.sig().params()) {
            param.attach()
        }
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
        Denotation.Type retType = township.resolveType(iFunc.sig().ret());
        OutExpr oExpr = new OutExpr(g.prefix(), returnStmt.expr());
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
}
