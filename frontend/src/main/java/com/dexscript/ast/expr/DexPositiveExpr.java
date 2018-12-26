package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexPositiveExpr extends DexUnaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 100;

    public DexPositiveExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == '+') {
                right = DexExpr.parse(src.slice(i + 1), RIGHT_RANK);
                return;
            }
            return;
        }
    }

    public static DexPositiveExpr $(String src) {
        return new DexPositiveExpr(new Text(src));
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }
}
