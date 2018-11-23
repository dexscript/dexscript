package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;

public class DexAddExpr implements DexBinaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;

    private final Text src;
    private final DexExpr left;
    private DexExpr right;

    // for walk up
    private DexElement parent;
    private DexStatement stmt;

    public DexAddExpr(Text src, DexExpr left) {
        this.left = left;
        this.src = src;
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

    public DexExpr left() {
        return left;
    }

    public DexExpr right() {
        return right;
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public DexStatement stmt() {
        return stmt;
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        DexBinaryOperator.reparentChildren(this);
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        return left().begin();
    }

    @Override
    public int end() {
        return right().end();
    }

    @Override
    public boolean matched() {
        return right != null && right.matched();
    }

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }
}
