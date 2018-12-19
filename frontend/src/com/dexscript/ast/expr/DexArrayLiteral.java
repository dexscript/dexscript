package com.dexscript.ast.expr;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexArrayLiteral extends DexExpr {

    private int arrayLiteralEnd = -1;

    public DexArrayLiteral(Text src) {
        super(src);
        new Parser();
    }

    public DexArrayLiteral(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int end() {
        if (arrayLiteralEnd == -1) {
            throw new IllegalStateException();
        }
        return arrayLiteralEnd;
    }

    @Override
    public boolean matched() {
        return arrayLiteralEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftBracket);
        }

        @Expect("[")
        State leftBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '[') {
                    i += 1;
                    return this::exprOrRightBracket;
                }
                return null;
            }
            return null;
        }

        @Expect("expression")
        @Expect("]")
        State exprOrRightBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ']') {
                    arrayLiteralEnd = i + 1;
                    return null;
                }
                return null;
            }
            return null;
        }
    }
}
