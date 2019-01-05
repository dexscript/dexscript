package com.dexscript.pkg;

import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.type.core.TypeSystem;

class CheckValueRef implements CheckSemanticError.Handler<DexValueRef> {
    @Override
    public void handle(CheckSemanticError cse, DexValueRef elem) {
        if (elem.isGlobalScope()) {
            CheckInvocation.$(cse, elem);
            return;
        }
        TypeSystem ts = cse.typeSystem();
        Value val = InferValue.$(ts, elem);
        if (val == null) {
            cse.report(elem, "referenced value not found: " + elem);
        }
    }
}
