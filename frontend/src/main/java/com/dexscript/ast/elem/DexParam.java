package com.dexscript.ast.elem;

import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexParenExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

public final class DexParam extends DexElement {

    private DexIdentifier paramName;
    private DexType paramType;
    private DexSyntaxError syntaxError;
    private int paramEnd = -1;

    public DexParam(Text src) {
        super(src);
        new Parser();
    }

    public DexIdentifier paramName() {
        return paramName;
    }

    public DexType paramType() {
        return paramType;
    }

    @Override
    public int begin() {
        if (paramName != null && paramName.matched()) {
            return paramName.begin();
        }
        throw new IllegalStateException();
    }

    @Override
    public int end() {
        if (paramEnd == -1) {
            throw new IllegalStateException();
        }
        return paramEnd;
    }

    @Override
    public boolean matched() {
        return paramEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (paramName() != null) {
            visitor.visit(paramName());
        }
        if (paramType() != null) {
            visitor.visit(paramType());
        }
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::paramName);
        }

        @Expect("identifier")
        State paramName() {
            paramName = new DexIdentifier(new Text(src.bytes, i, src.end));
            paramName.reparent(DexParam.this);
            if (paramName.matched()) {
                i = paramName.end();
                return this::colon;
            }
            return null;
        }

        @Expect(":")
        State colon() {
            for (;i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::paramType;
                }
                return null;
            }
            return null;
        }

        @Expect("type reference")
        State paramType() {
            paramType = DexType.parse(src.slice(i));
            paramType.reparent(DexParam.this);
            if (!paramType.matched()) {
                return this::missingParamType;
            }
            paramEnd = paramType.end();
            return null;
        }

        State missingParamType() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            for (;i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b) || LineEnd.$(b) || b == ',' || b == ')') {
                    paramEnd = i;
                    return null;
                }
            }
            paramEnd = i;
            return null;
        }
    }
}
