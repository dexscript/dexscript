package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexProduceExpr extends DexBinaryOperator {

    private static final int LEFT_RANK = 30;

    public DexProduceExpr(Text src, DexExpr left) {
        super(src, left);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, '-', '>')) {
                right = new DexValueRef(src.slice(i + 2));
                return;
            }
            // not ->
            return;
        }
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }
}
