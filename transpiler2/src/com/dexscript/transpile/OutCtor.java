package com.dexscript.transpile;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.ast.stmt.DexShortVarDecl;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.stmt.OutReturnStmt;
import com.dexscript.transpile.stmt.OutShortValDecl;

public class OutCtor {

    private final OutClass oClass;
    private final DexFunction iFunc;
    private final Gen g;
    private Town town;

    public OutCtor(OutClass oClass, DexFunction iFunc) {
        town = oClass.township();
        this.oClass = oClass;
        this.iFunc = iFunc;
        g = new Gen(oClass.indention());
        g.__("public "
        ).__(className()
        ).__(new OutSig(town, iFunc.sig()).toString()
        ).__(" {"
        ).__(new Indent(this::genBody)
        ).__(new Line("}"));
    }

    private void genBody() {
        for (DexParam param : iFunc.sig().params()) {
            OutField oField = oClass.allocateField(param.paramName(), town.resolveType(param.paramType()));
            param.attach(oField);
            g.__("this."
            ).__(oField.fieldName
            ).__(" = "
            ).__(param.paramName().toString()
            ).__(new Line(";"));
        }
        for (DexStatement stmt : iFunc.block().stmts()) {
            if (stmt instanceof DexReturnStmt) {
                new OutReturnStmt(this, g, (DexReturnStmt) stmt);
                continue;
            }
            if (stmt instanceof DexShortVarDecl) {
                new OutShortValDecl(this, g, (DexShortVarDecl) stmt);
                continue;
            }
            throw new UnsupportedOperationException("not implemented");
        }
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

    public Town town() {
        return town;
    }

    public OutClass oClass() {
        return oClass;
    }

    public DexFunction iFunc() {
        return iFunc;
    }
}
