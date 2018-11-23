package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.Keyword;
import com.dexscript.parser2.token.LineEnd;

public class DexReturnStmt implements DexStatement {

    private final Text src;
    private int returnBegin = -1;
    private int returnEnd = -1;
    private DexExpr expr;
    private DexError err;

    public DexReturnStmt(Text src) {
        this.src = src;
        new Parser();
    }

    public DexReturnStmt(String src) {
        this(new Text(src));
    }

    public DexExpr expr() {
        return expr;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        if (returnBegin == -1) {
            throw new IllegalStateException();
        }
        return returnBegin;
    }

    @Override
    public int end() {
        if (returnEnd == -1) {
            throw new IllegalStateException();
        }
        return returnEnd;
    }

    @Override
    public boolean matched() {
        return returnEnd != -1;
    }

    @Override
    public DexError err() {
        return err;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(expr);
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::returnKeyword);
        }

        @Expect("return")
        State returnKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i, 'r', 'e', 't', 'u', 'r', 'n')) {
                    returnBegin = i;
                    i += 6;
                    return this::blank;
                }
                break;
            }
            return null;
        }

        @Expect("blank")
        State blank() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::expr;
            }
            return null;
        }

        @Expect("expression")
        State expr() {
            expr = DexExpr.parse(src.slice(i));
            if (expr.matched()) {
                returnEnd = expr.end();
            } else {
                return this::missingExpr;
            }
            return null;
        }

        State missingExpr() {
            reportError();
            for (; i < src.end; i++) {
                if (LineEnd.__(src.bytes[i])) {
                    returnEnd = i;
                    return null;
                }
            }
            returnEnd = i;
            return null;
        }

        void reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
        }
    }
}
