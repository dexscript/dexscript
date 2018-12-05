package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.func.DexShortVarDecl;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class InferShortVarDecl implements InferValue {

    @Override
    public void handle(TypeSystem ts, DexElement elem, ValueTable table) {
        DexShortVarDecl shortVarDecl = (DexShortVarDecl) elem;
        if (shortVarDecl.decls().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        DexIdentifier decl = shortVarDecl.decls().get(0);
        String valueName = decl.toString();
        Type valueType = InferType.$(ts, shortVarDecl.expr());
        table.define(new Value(valueName, valueType, decl));
    }
}
