package com.dexscript.transpiler;

import com.dexscript.psi.DexServeStatement;

public class OutInnerClass extends OutClass {

    public OutInnerClass(DexServeStatement iServeStmt, OutMethod oMethod) {
        super(oMethod.iFile(), "", oMethod.oClass().oShim());
        appendReturnValueFields(iServeStmt.getSignature());
        OutMethod oSubMethod = new OutMethod(this, iServeStmt.getSignature());
        oSubMethod.append("{");
        oSubMethod.indent(() -> {
            iServeStmt.getBlock().acceptChildren(oSubMethod);
        });
        oSubMethod.appendNewLine('}');
        genClassBody();
    }
}
