package com.dexscript.parser2.expr;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.stmt.DexStatement;
import com.dexscript.parser2.token.Blank;

public class DexPositiveExpr implements DexExpr {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 100;
    private final Text src;
    private DexExpr right;

    // for walk up
    private DexElement parent;
    private DexStatement stmt;

    public DexPositiveExpr(Text src) {
        this.src = src;
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.__(b)) {
                continue;
            }
            if (b == '+') {
                right = DexExpr.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                return;
            }
            // not plus
            return;
        }
    }

    public DexPositiveExpr(String src) {
        this(new Text(src));
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        right.reparent(parent, stmt);
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public DexStatement stmt() {
        return stmt;
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
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
        return right.end();
    }

    @Override
    public boolean matched() {
        return right != null && right.matched();
    }

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(right);
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }
}
