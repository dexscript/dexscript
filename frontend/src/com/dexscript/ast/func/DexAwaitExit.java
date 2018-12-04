package com.dexscript.ast.func;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexAwaitExit extends DexAwaitCase {

    private int stmtEnd = -1;

    public DexAwaitExit(Text src) {
        super(src);
        for(int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, 'e', 'x', 'i', 't', '!')) {
                stmtEnd = i + 5;
                return;
            }
            return;
        }
    }

    public DexAwaitExit(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return stmtEnd;
    }

    @Override
    public boolean matched() {
        return stmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }
}
