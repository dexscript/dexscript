package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DexNewExpr extends DexExpr {

    private static final int LEFT_RANK = 0;

    private DexValueRef target;
    private int newExprEnd = -1;
    private DexSyntaxError syntaxError;
    private DexFunctionCallExpr functionCallExpr;

    public DexNewExpr(Text src) {
        super(src);
        new Parser();
    }

    public DexValueRef target() {
        return target;
    }

    public List<DexExpr> args() {
        if (functionCallExpr == null) {
            return Collections.emptyList();
        }
        List<DexExpr> args = functionCallExpr.args();
        if (args == null) {
            return Collections.emptyList();
        }
        return args;
    }

    public List<DexType> typeArgs() {
        if (functionCallExpr == null) {
            return Collections.emptyList();
        }
        List<DexType> typeArgs = functionCallExpr.typeArgs();
        if (typeArgs == null) {
            return Collections.emptyList();
        }
        return typeArgs;
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        if (target() != null) {
            target().reparent(this, stmt);
        }
        if (typeArgs() != null) {
            for (DexType typeArg : typeArgs()) {
                typeArg.reparent(this);
            }
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
        if (typeArgs() != null) {
            for (DexType typeArg : typeArgs()) {
                visitor.visit(typeArg);
            }
        }
        if (args() != null) {
            for (DexExpr arg : args()) {
                visitor.visit(arg);
            }
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
                    return this::blank;
                }
                return null;
            }
            return null;
        }

        @Expect("(")
        State blank() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::target;
        }

        @Expect("value reference")
        State target() {
            target = new DexValueRef(src.slice(i));
            if (!target.matched()) {
                return this::missingTarget;
            }
            i = target.end();
            return this::functionCall;
        }

        State functionCall() {
            functionCallExpr = new DexFunctionCallExpr(src.slice(i), DexNewExpr.this);
            if (!functionCallExpr.matched()) {
                return this::missingFunctionCall;
            }
            newExprEnd = functionCallExpr.end();
            return null;
        }

        State missingFunctionCall() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    newExprEnd = i;
                    return null;
                }
                if (b == ')') {
                    newExprEnd = i + 1;
                    return null;
                }
            }
            newExprEnd = i;
            return null;
        }

        State missingTarget() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    newExprEnd = i;
                    return null;
                }
                if (b == '(') {
                    return this::functionCall;
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
