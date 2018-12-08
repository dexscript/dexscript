package com.dexscript.transpile.body;

import com.dexscript.ast.func.DexVarDecl;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.Type;

public class TranslateVarDecl implements Translate<DexVarDecl> {
    @Override
    public void handle(OutClass oClass, DexVarDecl iVarDecl) {
        Type type = oClass.typeSystem().resolveType(iVarDecl.type());
        OutField oField = oClass.allocateField(iVarDecl.identifier().toString(), type);
        iVarDecl.identifier().attach(oField);
    }
}
