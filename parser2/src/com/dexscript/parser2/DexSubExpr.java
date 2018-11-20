package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;

public class DexSubExpr implements DexExpr {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;

    private final DexElement left;
    private final Text src;
    private DexElement right;

    public DexSubExpr(DexElement left, Text src) {
        this.left = left;
        this.src = src;
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
                continue;
            }
            if (b == '-') {
                right = DexExpr.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                if (right.matched()) {
                    return;
                }
                right = null;
                return;
            }
            // not plus
            return;
        }
    }

    public DexElement left() {
        return left;
    }

    public DexElement right() {
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
        return right != null;
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
