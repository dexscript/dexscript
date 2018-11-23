package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

public final class DexParenExpr extends DexExpr {

    private DexExpr body;
    private Text matched;

    public DexParenExpr(Text src) {
        super(src);
        new Parser();
    }

    public DexParenExpr(String src) {
        this(new Text(src));
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
    public DexError err() {
        return null;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(body);
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
            // TODO: handle error
            return null;
        }
    }
}
