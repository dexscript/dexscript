package com.dexscript.analyze;

import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.type.BuiltinTypes;

class CheckValueRef implements CheckSemanticError.Handler<DexValueRef> {
    @Override
    public void handle(CheckSemanticError cse, DexValueRef elem) {
        Value val = InferValue.$(cse.typeSystem(), elem);
        if (BuiltinTypes.UNDEFINED.equals(val.type())) {
            cse.report(elem, "referenced value not found: " + elem);
        }
    }
}
