package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.Separator;

public class DexBreakStmt extends DexStatement {

    private int breakStmtEnd = -1;

    public DexBreakStmt(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, 'b', 'r', 'e', 'a', 'k') && Separator.$(src, i+5)) {
                breakStmtEnd = i + 5;
            }
            return;
        }
    }

    @Override
    public int end() {
        if (breakStmtEnd == -1) {
            throw new IllegalStateException();
        }
        return breakStmtEnd;
    }

    @Override
    public boolean matched() {
        return breakStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }
}
