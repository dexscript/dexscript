package com.dexscript.parser2.expr;

import com.dexscript.parser2.DexElement;
import com.dexscript.parser2.DexError;
import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.One2Nine;
import com.dexscript.parser2.token.Zero2Nine;

public class DexIntegerLiteral implements DexExpr {

    private final Text src;
    private Text matched;

    public DexIntegerLiteral(Text src) {
        this.src = src;
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
    public Text src() {
        return src;
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

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
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
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '0') {
                    matched = new Text(src.bytes, i, i+1);
                    return null;
                }
                if (One2Nine.__(b)) {
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
                if (Zero2Nine.__(b)) {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }
    }
}
