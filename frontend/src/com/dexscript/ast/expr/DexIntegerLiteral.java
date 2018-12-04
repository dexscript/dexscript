package com.dexscript.ast.expr;

import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.One2Nine;
import com.dexscript.ast.token.Zero2Nine;

public class DexIntegerLiteral extends DexLeafExpr {

    private Text matched;

    public DexIntegerLiteral(Text src) {
        super(src);
        new Parser();
    }

    public DexIntegerLiteral(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    @Override
    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return matched != null;
    }

    private class Parser {

        int i = src.begin;
        int integerBegin = -1;

        Parser() {
            State.Play(this::firstChar);
        }

        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '0') {
                    matched = new Text(src.bytes, i, i + 1);
                    return null;
                }
                if (One2Nine.$(b)) {
                    integerBegin = i;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }
    }
}
