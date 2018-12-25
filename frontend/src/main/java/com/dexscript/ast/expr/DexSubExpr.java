package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexSubExpr extends DexBinaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;

    public DexSubExpr(Text src, DexExpr left) {
        super(src, left);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == '-') {
                right = DexExpr.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                return;
            }
            // not plus
            return;
        }
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }
}
