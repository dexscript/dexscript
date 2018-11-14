package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

class TransExpr extends DexVisitor {

    private final OutClass oClass;
    private final OutValue[] vals;

    public TransExpr(OutClass oClass, OutValue...vals) {
        this.oClass = oClass;
        this.vals = vals;
    }

    public static String translateOneValue(OutClass oClass, DexExpression expr) {
        OutValue val = new OutValue(oClass.iFile());
        expr.accept(new TransExpr(oClass, val));
        return val.toString();
    }

    private void expectOneValue(String s) {
        if (vals.length != 1) {
            throw new IllegalStateException(s);
        }
    }

    @Override
    public void visitNewExpr(@NotNull DexNewExpr o) {
        OutValue val = vals[0];
        val.type = new RuntimeType(RuntimeTypeKind.GENERIC_OBJECT, "Result");
        String funcName = o.getExpression().getNode().getText();
        oClass.referenced(o);
        val.append(oClass.shimClassName());
        val.append('.');
        val.append(funcName);
        val.append("()");
    }

    @Override
    public void visitUnaryExpr(@NotNull DexUnaryExpr o) {
        if (o.getGetResult() != null) {
            genGetResult(o.getExpression());
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    @Override
    public void visitReferenceExpression(@NotNull DexReferenceExpression o) {
        OutValue val = vals[0];
        val.append(o);
    }

    private void genGetResult(DexExpression o) {
        expectOneValue("get result can only assign to one value");
        OutValue subVal = new OutValue(oClass.iFile());
        trans(o, subVal);
        OutValue val = vals[0];
        val.append("((");
        val.append(val.type.className);
        val.append(")");
        val.append(subVal.toString());
        val.append(".result1__())");
    }

    private void trans(DexExpression expr, OutValue...vals) {
        expr.accept(new TransExpr(oClass, vals));
    }
}
