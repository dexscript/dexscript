package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;

public class DexEndExpr implements DexExpr {

    private final Text src;
    private int matchedEnd = -1;

    public DexEndExpr(Text src) {
        this.src = src;
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
                continue;
            }
            if (b == ';') {
                matchedEnd = i;
                return;
            }
            return;
        }
        matchedEnd = src.end;
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        if (matchedEnd == -1) {
            throw new IllegalStateException();
        }
        return matchedEnd;
    }

    @Override
    public boolean matched() {
        return matchedEnd != -1;
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
