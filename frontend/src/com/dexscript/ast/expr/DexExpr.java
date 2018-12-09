package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;

public abstract class DexExpr extends DexElement {

    protected DexStatement stmt;

    public DexExpr(Text src) {
        super(src);
    }

    public abstract int leftRank();

    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
    }

    public final DexStatement stmt() {
        return stmt;
    }

    @Override
    public final DexElement prev() {
        if (stmt() != null) {
            return stmt();
        } else {
            return parent();
        }
    }

    public static DexExpr parse(String src) {
        return parse(new Text(src), 0);
    }

    public static DexExpr parse(Text src) {
        return parse(src, 0);
    }

    public static DexExpr parse(Text src, int rightRank) {
        DexExpr left = parseLeft(src);
        if (!left.matched()) {
            return left;
        }
        while (true) {
            DexExpr expr = parseRight(src, left);
            if (!expr.matched()) {
                return left;
            }
            if (rightRank >= expr.leftRank()) {
                return left;
            }
            left = expr;
        }
    }

    private static DexExpr parseLeft(Text src) {
        DexExpr expr = new DexNewExpr(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexValueRef(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexConsumeExpr(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexPositiveExpr(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexNegativeExpr(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexParenExpr(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexStringLiteral(src);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexFloatLiteral(src);
        if (expr.matched()) {
            DexExpr intExpr = new DexIntegerLiteral(src);
            if (intExpr.matched() && intExpr.end() == expr.end()) {
                return intExpr;
            }
            return expr;
        }
        return new DexEndExpr(src);
    }

    private static DexExpr parseRight(Text src, DexExpr left) {
        src = new Text(src.bytes, left.end(), src.end);
        DexExpr expr = new DexAddExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexSubExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexMulExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexDivExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexMethodCallExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexFunctionCallExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexEqualExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        expr = new DexLessThanExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        return new DexEndExpr(src);
    }

    public DexFunctionCallExpr asFunctionCall() {
        return (DexFunctionCallExpr) this;
    }

    public DexValueRef asRef() {
        return (DexValueRef) this;
    }

    public DexMethodCallExpr asMethodCall() {
        return (DexMethodCallExpr) this;
    }
}
