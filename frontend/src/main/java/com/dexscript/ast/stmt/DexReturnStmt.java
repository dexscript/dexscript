package com.dexscript.ast.stmt;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

public class DexReturnStmt extends DexStatement {

    private int returnBegin = -1;
    private int returnEnd = -1;
    private DexExpr expr;
    private DexSyntaxError syntaxError;

    public DexReturnStmt(Text src) {
        super(src);
        new Parser();
    }

    public static DexReturnStmt $(String src) {
        return new DexReturnStmt(new Text(src));
    }

    public DexExpr expr() {
        return expr;
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
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(expr);
    }

    public DexSig enclosingSig() {
        DexElement current = parent;
        while (current != null) {
            if (current instanceof DexActor) {
                return ((DexActor) current).sig();
            }
            current = current.parent();
        }
        throw new DexSyntaxException("actor not found");
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
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'r', 'e', 't', 'u', 'r', 'n')) {
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
                if (Blank.$(b)) {
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
                expr.reparent(DexReturnStmt.this, DexReturnStmt.this);
                returnEnd = expr.end();
            } else {
                return this::missingExpr;
            }
            return null;
        }

        State missingExpr() {
            reportError();
            for (; i < src.end; i++) {
                if (LineEnd.$(src.bytes[i])) {
                    returnEnd = i;
                    return null;
                }
            }
            returnEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
