package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexGetResultExpr extends DexUnaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 100;

    public DexGetResultExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
                continue;
            }
            if (Keyword.__(src, i, '<', '-')) {
                right = DexExpr.parse(new Text(src.bytes, i + 2, src.end), RIGHT_RANK);
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
