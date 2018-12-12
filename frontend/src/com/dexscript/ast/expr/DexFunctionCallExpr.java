package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.List;

public class DexFunctionCallExpr extends DexExpr implements DexInvocationExpr {

    private static final int LEFT_RANK = 1;
    private static final int RIGHT_RANK = 0;

    private final DexExpr target;
    private List<DexExpr> args;
    private List<DexType> typeArgs;
    private int callExprEnd = -1;
    private DexSyntaxError syntaxError;
    private DexInvocation invocation;

    public DexFunctionCallExpr(Text src, DexExpr target) {
        super(src);
        this.target = target;
        new Parser();
    }

    public DexExpr target() {
        return target;
    }

    public List<DexExpr> args() {
        return args;
    }

    public List<DexType> typeArgs() {
        return typeArgs;
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
    public int begin() {
        return target().begin();
    }

    @Override
    public int end() {
        if (!matched()) {
            throw new IllegalStateException();
        }
        return callExprEnd;
    }

    @Override
    public boolean matched() {
        return callExprEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (target() != null) {
            visitor.visit(target());
        }
        if (args() != null) {
            for (DexExpr arg : args()) {
                visitor.visit(arg);
            }
        }
        if (typeArgs() != null) {
            for (DexType typeArg : typeArgs()) {
                visitor.visit(typeArg);
            }
        }
    }

    @Override
    public DexInvocation invocation() {
        if (invocation == null) {
            invocation = new DexInvocation(target().asRef().toString(), typeArgs(), args());
        }
        return invocation;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftAngleBracketOrLeftParen);
        }

        @Expect("<")
        @Expect("(")
        State leftAngleBracketOrLeftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    i += 1;
                    args = new ArrayList<>();
                    return this::argument;
                }
                if (b == '<') {
                    i += 1;
                    typeArgs = new ArrayList<>();
                    return this::typeArgument;
                }
                return null;
            }
            return null;
        }

        @Expect("type reference")
        State typeArgument() {
            DexType typeArg = DexType.parse(src.slice(i));
            typeArg.reparent(DexFunctionCallExpr.this);
            if (!typeArg.matched()) {
                return this::missingTypeArgument;
            }
            typeArgs.add(typeArg);
            i = typeArg.end();
            return this::moreTypeArgument;
        }

        @Expect(",")
        @Expect(">")
        State moreTypeArgument() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::typeArgument;
                }
                if (b == '>') {
                    i += 1;
                    return this::leftParen;
                }
                return this::missingRightAngleBracket;
            }
            return this::missingRightAngleBracket;
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
                    return this::argument;
                }
                return this::missingLeftParen;
            }
            return this::missingLeftParen;
        }

        @Expect("expression")
        State argument() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
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
                    return this::argument;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
                break;
            }
            i = originalCursor;
            return this::missingRightParen;
        }

        State missingTypeArgument() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::typeArgument;
                }
                if (b == '>') {
                    i += 1;
                    return this::leftParen;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingArgument() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::argument;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingRightAngleBracket() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::leftParen;
                }
                if (b == '(') {
                    i += 1;
                    return this::argument;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingLeftParen() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::argument;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        private State missingRightParen() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
