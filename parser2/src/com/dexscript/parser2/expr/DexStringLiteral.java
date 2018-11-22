package com.dexscript.parser2.expr;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.token.Blank;

public class DexStringLiteral implements DexExpr {

    private final Text src;
    private Text matched;

    public DexStringLiteral(Text src) {
        this.src = src;
        new Parser();
    }

    public DexStringLiteral(String src) {
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
        int strBegin = -1;

        Parser() {
            State.Play(this::leftQuote);
        }

        @Expect("'")
        State leftQuote() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '\'') {
                    strBegin = i;
                    i += 1;
                    return this::body;
                }
                break;
            }
            return null;
        }

        State body() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == '\'') {
                    matched = src.slice(strBegin, i+1);
                    return null;
                }
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
