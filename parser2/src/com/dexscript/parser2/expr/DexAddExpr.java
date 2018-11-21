package com.dexscript.parser2.expr;

import com.dexscript.parser2.DexElement;
import com.dexscript.parser2.DexError;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;

public class DexAddExpr implements DexBinaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;

    private final Text src;
    private final DexExpr left;
    private DexExpr right;

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
