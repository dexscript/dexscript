package com.dexscript.transpile.shim.impl;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.type.InnerActorType;
import com.dexscript.type.TypeSystem;

public class HasAwait implements DexElement.Visitor {

    private boolean result;
    private final TypeSystem ts;

    public HasAwait(TypeSystem ts, DexElement func) {
        this.ts = ts;
        visit(func);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem instanceof DexAwaitConsumer) {
            result = true;
            return;
        }
        if (elem instanceof DexConsumeExpr) {
            result = true;
            return;
        }
        if (elem instanceof DexProduceStmt) {
            // do not visit DexValueRef inside Produce
            return;
        }
        if (elem instanceof DexValueRef) {
            if (InferValue.$(ts, (DexValueRef) elem).type() instanceof InnerActorType) {
                result = true;
                return;
            }
        }
        elem.walkDown(this);
    }

    public boolean result() {
        return result;
    }
}
