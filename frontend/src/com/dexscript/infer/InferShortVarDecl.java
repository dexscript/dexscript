package com.dexscript.infer;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.expr.DexIntegerLiteral;
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
        Type valueType = InferType.$(ts, shortVarDecl.expr());
        // widen literal types
        if (valueType instanceof StringLiteralType) {
            valueType = BuiltinTypes.STRING;
        } else if (valueType instanceof IntegerLiteralType) {
            valueType = BuiltinTypes.INT64;
        }
        table.define(new Value(valueName, valueType, decl));
    }
}
