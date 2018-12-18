package com.dexscript.ast.inf;

import com.dexscript.ast.DexInterfaceBody;
import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

public class DexInfTypeParam extends DexElement {

    private DexIdentifier paramName;
    private DexType paramType;
    private int typeParamEnd = -1;
    private DexSyntaxError syntaxError;

    public DexInfTypeParam(Text src) {
        super(src);
        new Parser();
    }

    public DexInfTypeParam(String src) {
        this(new Text(src));
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
    public void walkDown(Visitor visitor) {
        if (paramName() != null) {
            visitor.visit(paramName());
        }
        if (paramType() != null) {
            visitor.visit(paramType());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexIdentifier paramName() {
        return paramName;
    }

    public DexType paramType() {
        return paramType;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftAngleBracket);
        }

        @Expect("<")
        State leftAngleBracket() {
            for (; i< src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '<') {
                    i += 1;
                    return this::identifier;
                }
                return null;
            }
            return null;
        }

        @Expect("paramName")
        State identifier() {
            paramName = new DexIdentifier(src.slice(i));
            paramName.reparent(DexInfTypeParam.this);
            if (!paramName.matched()) {
                return this::missingIdentifier;
            }
            i = paramName.end();
            return this::rightAngleBracket;
        }

        @Expect(">")
        State rightAngleBracket() {
            for (; i< src.end;i++) {
                byte b= src.bytes[i];
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
            for (; i< src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::type;
                }
                return this::missingColon;
            }
            return this::missingColon;
        }

        @Expect("type")
        State type() {
            paramType = DexType.parse(src.slice(i));
            paramType.reparent(DexInfTypeParam.this);
            if (!paramType.matched()) {
                return this::missingType;
            }
            typeParamEnd = paramType.end();
            return null;
        }

        State missingType() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    typeParamEnd = i;
                    return null;
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
                    return this::type;
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
                    return this::type;
                }
            }
            typeParamEnd = i;
            return null;
        }

        State missingIdentifier() {
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

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
