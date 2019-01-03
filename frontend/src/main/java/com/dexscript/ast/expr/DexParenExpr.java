package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

public final class DexParenExpr extends DexExpr {

    private DexSyntaxError syntaxError;
    private DexExpr body;
    private Text matched;

    public DexParenExpr(Text src) {
        super(src);
        new Parser();
    }

    public static DexParenExpr $(String src) {
        return new DexParenExpr(new Text(src));
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        body.reparent(parent, stmt);
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

    public DexExpr body() {
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
            body = DexExpr.parse(src.slice(i), 0);
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
                // reset and search line end instead
                i = savedCursor;
                return this::lineEnd;
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
