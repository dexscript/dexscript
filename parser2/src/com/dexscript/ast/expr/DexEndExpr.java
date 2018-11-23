package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;

public class DexEndExpr implements DexLeafExpr {

    private final Text src;
    private int matchedEnd;

    // for walk up
    private DexElement parent;
    private DexStatement stmt;

    public DexEndExpr(Text src) {
        this.src = src;
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
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
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public DexStatement stmt() {
        return stmt;
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
        return matchedEnd;
    }

    @Override
    public boolean matched() {
        return false;
    }

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public String toString() {
        return "<error/>";
    }
}
