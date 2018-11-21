package com.dexscript.parser2.expr;

import com.dexscript.parser2.DexElement;
import com.dexscript.parser2.DexError;
import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.LineEnd;

public class DexParenExpr implements DexExpr {

    private final Text src;
    private DexExpr body;
    private Text matched;

    public DexParenExpr(Text src) {
        this.src = src;
        new Parser();
    }

    public DexParenExpr(String src) {
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

    public DexExpr body() {
        return body;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;
        int parenBegin = -1;

        Parser() {
            State.Play(this::leftParen);
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '(') {
                    parenBegin = i;
                    i += 1;
                    return this::body;
                }
            }
            return null;
        }

        @Expect("expr")
        State body() {
            body = DexExpr.parse(new Text(src.bytes, i, src.end), 0);
            if (body.matched()) {
                i = body.end();
                return this::rightParen;
            }
            reportError();
            return this::rightParen;
        }

        @Expect(")")
        State rightParen() {
            int savedCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ')') {
                    matched = new Text(src.bytes, parenBegin, i+1);
                    return null;
                }
            }
            // reset and search line end instead
            i = savedCursor;
            return this::lineEnd;
        }

        @Expect(")")
        State lineEnd() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.__(b)) {
                    matched = new Text(src.bytes, parenBegin, i);
                    return reportError();
                }
                if (Blank.__(b)) {
                    continue;
                }
            }
            return reportError();
        }

        State reportError() {
            return null;
        }
    }
}