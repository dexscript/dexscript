package com.dexscript.transpile.type.actor;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.infer.InferType;
import com.dexscript.infer.InferValue;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.*;

import java.util.List;

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
        if (elem instanceof DexInvocationExpr) {
            DexInvocation invocation = ((DexInvocationExpr) elem).invocation();
            List<DType> args = InferType.inferTypes(ts, invocation.args());
            List<FunctionSig.Invoked> invokeds = ts.invoke(new Invocation(invocation.funcName(), null, args, null));
            for (FunctionSig.Invoked invoked : invokeds) {
                FunctionImpl impl = (FunctionImpl) invoked.function().attachment();
                if (impl.hasAwait()) {
                    result = true;
                    return;
                }
            }
        }
        elem.walkDown(this);
    }

    public boolean result() {
        return result;
    }
}
