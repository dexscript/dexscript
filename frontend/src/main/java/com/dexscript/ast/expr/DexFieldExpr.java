package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

import java.util.Arrays;

public class DexFieldExpr extends DexBinaryOperator implements DexInvocationExpr {

    private static final int LEFT_RANK = 10;
    private DexInvocation invocation;

    public DexFieldExpr(Text src, DexExpr left) {
        super(src, left);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == '.') {
                right = new DexValueRef(new Text(src.bytes, i + 1, src.end));
                return;
            }
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
            String fieldName = right().toString();
            String funcName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            invocation = new DexInvocation(pkg(), funcName, Arrays.asList(left()));
        }
        return invocation;
    }
}
