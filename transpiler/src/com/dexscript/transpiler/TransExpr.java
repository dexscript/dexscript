package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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

    public static String translateOneValue(TranspiledClass tClass, DexExpression expr) {
        ExpectedValue val = new ExpectedValue();
        expr.accept(new TransExpr(tClass, Arrays.asList(val)));
        return val.out.toString();
    }

    @Override
    public void visitLiteral(@NotNull DexLiteral o) {
        if (expectedValues.size() != 1) {
            throw new IllegalStateException("literal can only assign to one value");
        }
        ExpectedValue val = expectedValues.get(0);
        val.out.append("((long)");
        val.out.append(o.getNode().getText());
        val.out.append(')');
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
    public void visitAddExpr(@NotNull DexAddExpr o) {
        String funcName = "add";
        tClass.referenced(o);
        String fieldName = tClass.addField("addResult", "Result");
        tClass.append(fieldName);
        tClass.append(" = ");
        tClass.append(tClass.shimClassName());
        tClass.append('.');
        tClass.append(funcName);
        tClass.append('(');
        tClass.append(translateOneValue(tClass, o.getLeft()));
        tClass.append(',');
        tClass.append(translateOneValue(tClass, o.getRight()));
        tClass.appendNewLine(");");
        ExpectedValue val = expectedValues.get(0);
        val.out.append("((");
        val.out.append(val.type.className);
        val.out.append(")");
        val.out.append(fieldName);
        val.out.append(".result1__())");
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr o) {
        String funcName = o.getExpression().getNode().getText();
        tClass.referenced(o);
        String fieldName = tClass.addField(funcName, "Result");
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
