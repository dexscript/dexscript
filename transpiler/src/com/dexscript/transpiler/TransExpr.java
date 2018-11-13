package com.dexscript.transpiler;

import com.dexscript.psi.DexCallExpr;
import com.dexscript.psi.DexStringLiteral;
import com.dexscript.psi.DexVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class TransExpr extends DexVisitor {

    static class ExpectedValue {
        public RuntimeType type;
        public TranspiledCode out = new TranspiledCode();
    }

    private final TranspiledClass tClass;
    private final List<ExpectedValue> expectedValues;

    public TransExpr(TranspiledClass tClass, List<ExpectedValue> expectedValues) {
        this.tClass = tClass;
        this.expectedValues = expectedValues;
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        if (expectedValues.size() != 1) {
            throw new IllegalStateException("string literal can only assign to one value");
        }
        ExpectedValue val = expectedValues.get(0);
        String text = o.getNode().getText();
        val.out.append('"');
        val.out.append(text.substring(1, text.length() - 1));
        val.out.append('"');
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr o) {
        String funcName = o.getExpression().getNode().getText();
        tClass.referenced(o);
        String fieldName = tClass.assignResultField(funcName);
        tClass.append(fieldName);
        tClass.append(" = ");
        tClass.append(tClass.shimClassName());
        tClass.append('.');
        tClass.append(funcName);
        tClass.append("();");
        tClass.appendNewLine();
        ExpectedValue val = expectedValues.get(0);
        val.out.append("((");
        val.out.append(val.type.className);
        val.out.append(")");
        val.out.append(fieldName);
        val.out.append(".result1__())");
    }
}
