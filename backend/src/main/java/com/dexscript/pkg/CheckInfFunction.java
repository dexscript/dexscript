package com.dexscript.pkg;

import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.type.TypeTable;

public class CheckInfFunction implements CheckSemanticError.Handler<DexInfFunction> {

    @Override
    public void handle(CheckSemanticError cse, DexInfFunction elem) {
        TypeTable localTypeTable = new TypeTable(cse.typeSystem(), cse.localTypeTable(), elem.sig());
        cse.withTypeTable(localTypeTable, () -> elem.walkDown(cse));
    }
}
