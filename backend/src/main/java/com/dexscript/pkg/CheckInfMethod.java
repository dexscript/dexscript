package com.dexscript.pkg;

import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.type.core.TypeTable;

public class CheckInfMethod implements CheckSemanticError.Handler<DexInfMethod> {

    @Override
    public void handle(CheckSemanticError cse, DexInfMethod elem) {
        TypeTable localTypeTable = new TypeTable(cse.typeSystem(), cse.localTypeTable(), elem.sig());
        cse.withTypeTable(localTypeTable, () -> elem.walkDown(cse));
    }
}
