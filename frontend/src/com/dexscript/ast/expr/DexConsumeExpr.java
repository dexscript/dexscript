package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

import java.util.Arrays;

public class DexConsumeExpr extends DexUnaryOperator implements DexInvocationExpr {

    private static final int LEFT_RANK = 10;
    private DexInvocation invocation;

    public DexConsumeExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, '<', '-')) {
                right = DexExpr.parse(new Text(src.bytes, i + 2, src.end), 0);
                return;
            }
            // not <-
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
            invocation = new DexInvocation(pkg(), "Consume__", Arrays.asList(right()));
        }
        return invocation;
    }
}
