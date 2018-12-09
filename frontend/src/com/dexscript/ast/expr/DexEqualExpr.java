package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexEqualExpr extends DexBinaryOperator implements DexInvocationExpr {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;
    private DexInvocation invocation;

    public DexEqualExpr(Text src, DexExpr left) {
        super(src, left);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, '=', '=')) {
                right = DexExpr.parse(new Text(src.bytes, i + 2, src.end), RIGHT_RANK);
                return;
            }
            // not ==
            return;
        }
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public DexInvocation invocation() {
        if (invocation == null) {
            invocation = new DexInvocation("Equal__", left(), right());
        }
        return invocation;
    }
}
