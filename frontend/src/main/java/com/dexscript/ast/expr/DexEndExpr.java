package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexEndExpr extends DexLeafExpr {

    private int matchedEnd;

    public DexEndExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == ';') {
                matchedEnd = i + 1;
                return;
            }
            matchedEnd = i;
            return;
        }
        matchedEnd = src.end;
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return matchedEnd;
    }

    @Override
    public boolean matched() {
        return false;
    }

    @Override
    public String toString() {
        return "<error/>";
    }
}
