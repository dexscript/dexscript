package com.dexscript.ast.type;

import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

public final class DexParenType extends DexType {

    private DexSyntaxError syntaxError;
    private DexType body;
    private Text matched;

    public DexParenType(Text src) {
        super(src);
        new Parser();
    }

    public static DexParenType $(String src) {
        return new DexParenType(new Text(src));
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

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(body);
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexType body() {
        return body;
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
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    parenBegin = i;
                    i += 1;
                    return this::body;
                }
                return null;
            }
            return null;
        }

        @Expect("expr")
        State body() {
            body = DexType.parse(src.slice(i), 0);
            body.reparent(DexParenType.this);
            if (!body.matched()) {
                reportError();
                return this::rightParen;
            }
            i = body.end();
            return this::rightParen;
        }

        @Expect(")")
        State rightParen() {
            int savedCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
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
                if (LineEnd.$(b)) {
                    matched = new Text(src.bytes, parenBegin, i);
                    return reportError();
                }
                if (Blank.$(b)) {
                    continue;
                }
            }
            return reportError();
        }

        State reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            return null;
        }
    }
}
