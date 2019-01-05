package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexVarDecl;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;

public class TranslateVarDecl implements Translate<DexVarDecl> {
    @Override
    public void handle(OutClass oClass, DexVarDecl iVarDecl) {
        DType type = ResolveType.$(oClass.typeSystem(), null, iVarDecl.type());
        OutField oField = oClass.allocateField(iVarDecl.identifier().toString());
        iVarDecl.identifier().attach(oField);
        String initValue = type.initValue();
        if (initValue != null) {
            oClass.g().__(oField.value()
            ).__(" = "
            ).__(initValue
            ).__(new Line(";"));
        }
    }
}
