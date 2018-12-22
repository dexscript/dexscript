package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DexFunctionCallExpr extends DexExpr implements DexInvocationExpr {

    private static final int LEFT_RANK = 1;
    private static final int RIGHT_RANK = 0;

    private final DexExpr target;
    private List<DexExpr> posArgs;
    private List<DexType> typeArgs;
    private List<DexNamedArg> namedArgs;
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

    public List<DexExpr> posArgs() {
        if (posArgs == null) {
            return Collections.emptyList();
        }
        return posArgs;
    }

    public List<DexType> typeArgs() {
        if (typeArgs == null) {
            return Collections.emptyList();
        }
        return typeArgs;
    }

    public List<DexNamedArg> namedArgs() {
        if (namedArgs == null) {
            return Collections.emptyList();
        }
        return namedArgs;
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        if (target() != null) {
            target().reparent(this, stmt);
        }
        if (posArgs() != null) {
            for (DexExpr arg : posArgs()) {
                arg.reparent(this, stmt);
            }
        }
        if (namedArgs() != null) {
            for (DexNamedArg namedArg : namedArgs()) {
                namedArg.name().reparent(this);
                namedArg.val().reparent(this, stmt);
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
        if (posArgs() != null) {
            for (DexExpr arg : posArgs()) {
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
            invocation = new DexInvocation(target().asRef().toString(), typeArgs(), posArgs(), namedArgs());
        }
        return invocation;
    }

    private class Parser {

        int i = src.begin;
        DexIdentifier namedArgName;

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
                    posArgs = new ArrayList<>();
                    return this::posArg;
                }
                if (b == '<') {
                    i += 1;
                    typeArgs = new ArrayList<>();
                    return this::typeArg;
                }
                return null;
            }
            return null;
        }

        @Expect("type")
        State typeArg() {
            DexType typeArg = DexType.parse(src.slice(i));
            typeArg.reparent(DexFunctionCallExpr.this);
            if (!typeArg.matched()) {
                return this::missingTypeArg;
            }
            typeArgs.add(typeArg);
            i = typeArg.end();
            return this::moreTypeArg;
        }

        @Expect(",")
        @Expect(">")
        State moreTypeArg() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::typeArg;
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
                    posArgs = new ArrayList<>();
                    return this::posArg;
                }
                return this::missingLeftParen;
            }
            return this::missingLeftParen;
        }

        @Expect("expression")
        State posArg() {
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
            posArgs.add(arg);
            if (!arg.matched()) {
                return this::missingPosArg;
            }
            i = arg.end();
            return this::morePosArg;
        }

        @Expect("=")
        @Expect(",")
        @Expect(")")
        State morePosArg() {
            int originalCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '=') {
                    i += 1;
                    DexExpr lastPosArg = posArgs.remove(posArgs.size() - 1);
                    namedArgName = new DexIdentifier(src.slice(lastPosArg.begin(), lastPosArg.end()));
                    if (!namedArgName.matched() && syntaxError == null) {
                        syntaxError = new DexSyntaxError(src, lastPosArg.begin());
                    }
                    namedArgs = new ArrayList<>();
                    return this::namedArgVal;
                }
                if (b == ',') {
                    i += 1;
                    return this::posArg;
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

        @Expect("identifier")
        State namedArgName() {
            namedArgName = new DexIdentifier(src.slice(i));
            if (!namedArgName.matched()) {
                return this::missingNamedArgName;
            }
            i = namedArgName.end();
            return this::equal;
        }

        @Expect("=")
        State equal() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '=') {
                    i += 1;
                    return this::namedArgVal;
                }
                return null;
            }
            return null;
        }

        @Expect("expression")
        State namedArgVal() {
            DexExpr val = DexExpr.parse(src.slice(i));
            if (!val.matched()) {
                return this::missingNamedArgVal;
            }
            namedArgs.add(new DexNamedArg(namedArgName, val));
            i = val.end();
            return this::moreNamedArg;
        }

        @Expect(",")
        @Expect(")")
        State moreNamedArg() {
            int originalCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::namedArgName;
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

        State missingNamedArgName() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::equal;
                }
                if (b == ',') {
                    i += 1;
                    return this::namedArgName;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingNamedArgVal() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::moreNamedArg;
                }
                if (b == ',') {
                    i += 1;
                    return this::namedArgName;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingTypeArg() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    callExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::typeArg;
                }
                if (b == '>') {
                    i += 1;
                    return this::leftParen;
                }
            }
            callExprEnd = i;
            return null;
        }

        State missingPosArg() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::posArg;
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
                    return this::posArg;
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
                    return this::posArg;
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
