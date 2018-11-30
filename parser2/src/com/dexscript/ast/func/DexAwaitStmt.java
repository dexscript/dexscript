package com.dexscript.ast.func;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexAwaitStmt extends DexStatement {

    private int awaitEnd = -1;

    public DexAwaitStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexAwaitStmt(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return awaitEnd;
    }

    @Override
    public boolean matched() {
        return awaitEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::await);
        }

        @Expect("await")
        State await() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i, 'a', 'w', 'a', 'i', 't')) {
                    i += 6;
                    return this::leftBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("{")
        State leftBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    return this::casesOrRightBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("case")
        private State casesOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '}') {
                    awaitEnd = i + 1;
                    return null;
                }
                return null;
            }
            return null;
        }
    }
}
