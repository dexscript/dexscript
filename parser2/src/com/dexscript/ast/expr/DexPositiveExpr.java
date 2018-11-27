package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.func.DexStatement;
import com.dexscript.ast.token.Blank;

public class DexPositiveExpr extends DexExpr {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 100;
    private DexExpr right;

    public DexPositiveExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
                continue;
            }
            if (b == '+') {
                right = DexExpr.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                return;
            }
            // not plus
            return;
        }
    }

    public DexPositiveExpr(String src) {
        this(new Text(src));
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        right.reparent(parent, stmt);
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return right.end();
    }

    @Override
    public boolean matched() {
        return right != null && right.matched();
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(right);
    }
}
