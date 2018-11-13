package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

class TransExpr extends DexVisitor {

    private final TranspiledClass tClass;
    private final TranspiledValue[] vals;

    public TransExpr(TranspiledClass tClass, TranspiledValue ...vals) {
        this.tClass = tClass;
        this.vals = vals;
    }

    public static String translateOneValue(TranspiledClass tClass, DexExpression expr) {
        TranspiledValue val = new TranspiledValue();
        expr.accept(new TransExpr(tClass, val));
        return val.out.toString();
    }

    @Override
    public void visitLiteral(@NotNull DexLiteral o) {
        expectOneValue("literal can only assign to one value");
        TranspiledValue val = vals[0];
        val.out.append("((long)");
        val.out.append(o.getNode().getText());
        val.out.append(')');
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        expectOneValue("string literal can only assign to one value");
        TranspiledValue val = vals[0];
        String text = o.getNode().getText();
        val.out.append('"');
        val.out.append(text.substring(1, text.length() - 1));
        val.out.append('"');
    }

    private void expectOneValue(String s) {
        if (vals.length != 1) {
            throw new IllegalStateException(s);
        }
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
        TranspiledValue val = vals[0];
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
        TranspiledValue val = vals[0];
        val.out.append("((");
        val.out.append(val.type.className);
        val.out.append(")");
        val.out.append(fieldName);
        val.out.append(".result1__())");
    }

    @Override
    public void visitNewExpr(@NotNull DexNewExpr o) {
        TranspiledValue val = vals[0];
        val.type = new RuntimeType(RuntimeTypeKind.GENERIC_OBJECT, "Result");
        String funcName = o.getExpression().getNode().getText();
        tClass.referenced(o);
        val.out.append(tClass.shimClassName());
        val.out.append('.');
        val.out.append(funcName);
        val.out.append("()");
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
        TranspiledValue val = vals[0];
        val.out.append(o);
    }

    private void genGetResult(DexExpression o) {
        expectOneValue("get result can only assign to one value");
        TranspiledValue subVal = new TranspiledValue();
        trans(o, subVal);
        TranspiledValue val = vals[0];
        val.out.append("((");
        val.out.append(val.type.className);
        val.out.append(")");
        val.out.append(subVal.out.toString());
        val.out.append(".result1__())");
    }

    private void trans(DexExpression expr, TranspiledValue ...vals) {
        expr.accept(new TransExpr(tClass, vals));
    }
}
