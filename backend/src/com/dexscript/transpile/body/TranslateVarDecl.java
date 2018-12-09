package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexVarDecl;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.Int64Type;
import com.dexscript.type.Type;

public class TranslateVarDecl implements Translate<DexVarDecl> {
    @Override
    public void handle(OutClass oClass, DexVarDecl iVarDecl) {
        Type type = oClass.typeSystem().resolveType(iVarDecl.type());
        OutField oField = oClass.allocateField(iVarDecl.identifier().toString(), type);
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
