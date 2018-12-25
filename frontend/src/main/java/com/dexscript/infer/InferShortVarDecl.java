package com.dexscript.infer;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.stmt.DexShortVarDecl;
import com.dexscript.type.*;

public class InferShortVarDecl implements InferValue<DexShortVarDecl> {

    @Override
    public void handle(TypeSystem ts, DexShortVarDecl shortVarDecl, ValueTable table) {
        if (shortVarDecl.decls().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        DexIdentifier decl = shortVarDecl.decls().get(0);
        String valueName = decl.toString();
        DType valueType = InferType.$(ts, shortVarDecl.expr());
        // widen const types
        if (ts.isStringConst(valueType)) {
            valueType = ts.STRING;
        } else if (ts.isIntegerConst(valueType)) {
            valueType = ts.INT64;
        } else if (ts.isFloatConst(valueType)) {
            valueType = ts.FLOAT64;
        } else if (ts.isBoolConst(valueType)) {
            valueType = ts.BOOL;
        }
        table.define(new Value(valueName, valueType, decl));
    }
}
