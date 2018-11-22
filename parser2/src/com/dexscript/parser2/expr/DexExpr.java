package com.dexscript.parser2.expr;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.stmt.DexStatement;

public interface DexExpr extends DexStatement {

    int leftRank();

    static DexExpr parse(String src) {
        return parse(new Text(src), 0);
    }

    static DexExpr parse(Text src) {
        return parse(src, 0);
    }

    static DexExpr parse(Text src, int rightRank) {
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
        DexExpr expr = new DexReference(src);
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
        expr = new DexCallExpr(src, left);
        if (expr.matched()) {
            return expr;
        }
        return new DexEndExpr(src);
    }
}
