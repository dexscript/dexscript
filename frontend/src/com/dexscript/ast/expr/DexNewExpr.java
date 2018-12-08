package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexNewExpr extends DexExpr {

    private static final int LEFT_RANK = 0;
    private static final int RIGHT_RANK = 1;

    private DexExpr target;
    private List<DexExpr> args;
    private int newExprEnd = -1;
    private DexSyntaxError syntaxError;

    public DexNewExpr(Text src) {
        super(src);
        new Parser();
    }

    public DexExpr target() {
        return target;
    }

    public List<DexExpr> args() {
        return args;
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        if (target() != null) {
            target().reparent(this, stmt);
        }
        if (args() != null) {
            for (DexExpr arg : args()) {
                arg.reparent(this, stmt);
            }
        }
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int end() {
        if (!matched()) {
            throw new IllegalStateException();
        }
        return newExprEnd;
    }

    @Override
    public boolean matched() {
        return newExprEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    @Override
    public void walkDown(DexElement.Visitor visitor) {
        visitor.visit(target);
        for (DexExpr arg : args) {
            visitor.visit(arg);
        }
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::newKeyword);
        }

        @Expect("new")
        State newKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i,'n', 'e', 'w')) {
                    i += 3;
                    return this::blankOrLeftParen;
                }
                return null;
            }
            return null;
        }

        @Expect("(")
        State blankOrLeftParen() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    return this::target;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::target;
        }

        @Expect("expression")
        State target() {
            target = DexExpr.parse(src.slice(i), RIGHT_RANK);
            if (!target.matched()) {
                return this::missingTarget;
            }
            i = target.end();
            return this::leftParen;
        }

        private State missingTarget() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    newExprEnd = i;
                    return null;
                }
                if (b == '(') {
                    return this::leftParen;
                }
            }
            newExprEnd = i;
            return null;
        }


        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    i += 1;
                    args = new ArrayList<>();
                    return this::argumentOrRightParen;
                }
                break;
            }
            return null;
        }

        @Expect("expression")
        @Expect(")")
        State argumentOrRightParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ')') {
                    newExprEnd = i + 1;
                    return null;
                }
                break;
            }
            DexExpr arg = DexExpr.parse(new Text(src.bytes, i, src.end), RIGHT_RANK);
            args.add(arg);
            if (!arg.matched()) {
                return this::missingArgument;
            }
            i = arg.end();
            return this::commaOrRightParen;
        }

        State missingArgument() {
            reportError();
            // try to recover from invalid argument
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::argumentOrRightParen;
                }
                if (b == ')') {
                    newExprEnd = i + 1;
                    return null;
                }
                if (LineEnd.$(b)) {
                    newExprEnd = i;
                    return null;
                }
            }
            newExprEnd = i;
            return null;
        }

        @Expect(",")
        @Expect(")")
        State commaOrRightParen() {
            int originalCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::argumentOrRightParen;
                }
                if (b == ')') {
                    newExprEnd = i + 1;
                    return null;
                }
                break;
            }
            i = originalCursor;
            return this::missingRightBrace;
        }

        private State missingRightBrace() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    newExprEnd = i;
                    return null;
                }
            }
            newExprEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
