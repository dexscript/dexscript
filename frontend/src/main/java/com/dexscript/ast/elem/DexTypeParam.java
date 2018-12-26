package com.dexscript.ast.elem;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

public class DexTypeParam extends DexElement {

    private DexSyntaxError syntaxError;
    private DexIdentifier paramName;
    private DexType paramType;
    private int typeParamEnd = -1;

    public DexTypeParam(Text src) {
        super(src);
        new Parser();
    }

    public static DexTypeParam $ (String src) {
        return new DexTypeParam(new Text(src));
    }

    public DexIdentifier paramName() {
        return paramName;
    }

    public DexType paramType() {
        return paramType;
    }

    @Override
    public int end() {
        if (typeParamEnd == -1) {
            throw new IllegalStateException();
        }
        return typeParamEnd;
    }

    @Override
    public boolean matched() {
        return typeParamEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
        if (paramName() != null) {
            paramName().reparent(this);
        }
        if (paramType() != null) {
            paramType().reparent(this);
        }
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
            State.Play(this::leftAngleBracket);
        }

        @Expect("<")
        State leftAngleBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '<') {
                    i += 1;
                    return this::paramName;
                }
                return null;
            }
            return null;
        }

        @Expect("paramName")
        State paramName() {
            paramName = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (!paramName.matched()) {
                return this::missingParamName;
            }
            i = paramName.end();
            return this::rightAngleBracket;
        }

        @Expect(">")
        State rightAngleBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '>') {
                    i += 1;
                    return this::colon;
                }
                return this::missingRightAngleBracket;
            }
            return this::missingRightAngleBracket;
        }

        @Expect(":")
        State colon() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::paramType;
                }
                return this::missingColon;
            }
            return this::missingColon;
        }

        @Expect("reference")
        State paramType() {
            paramType = DexType.parse(src.slice(i));
            if (!paramType.matched()) {
                return this::missingParamType;
            }
            typeParamEnd = paramType.end();
            return null;
        }

        State missingParamType() {
            reportError();
            for (;i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b) || LineEnd.$(b) || b == ',' || b == ')') {
                    typeParamEnd = i;
                    return null;
                }
            }
            typeParamEnd = i;
            return null;
        }

        State missingRightAngleBracket() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    typeParamEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::colon;
                }
                if (b == ':') {
                    i += 1;
                    return this::paramType;
                }
            }
            typeParamEnd = i;
            return null;
        }

        State missingParamName() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    typeParamEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::rightAngleBracket;
                }
                if (b == '>') {
                    i += 1;
                    return this::colon;
                }
            }
            typeParamEnd = i;
            return null;
        }

        State missingColon() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    typeParamEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::paramType;
                }
            }
            typeParamEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
