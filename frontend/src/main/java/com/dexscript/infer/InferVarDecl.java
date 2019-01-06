package com.dexscript.infer;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.stmt.DexVarDecl;
import com.dexscript.type.core.*;

public class InferVarDecl implements InferValue<DexVarDecl> {

    @Override
    public void handle(TypeSystem ts, DexVarDecl varDecl, ValueTable table) {
        DexIdentifier decl = varDecl.identifier();
        String valueName = decl.toString();
        DType valueType = InferType.$(ts, varDecl.type());
        table.define(new Value(valueName, valueType, decl));
    }
}
