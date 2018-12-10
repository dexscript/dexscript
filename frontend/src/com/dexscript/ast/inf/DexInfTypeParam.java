package com.dexscript.ast.inf;

import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

public class DexInfTypeParam extends DexElement {

    private DexIdentifier identifier;
    private DexType type;
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
        if (identifier() != null) {
            visitor.visit(identifier());
        }
        if (type() != null) {
            visitor.visit(type());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public DexType type() {
        return type;
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

        @Expect("identifier")
        State identifier() {
            identifier = new DexIdentifier(src.slice(i));
            if (!identifier.matched()) {
                return this::missingIdentifier;
            }
            i = identifier.end();
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
            type = DexType.parse(src.slice(i));
            if (!type.matched()) {
                return this::missingType;
            }
            typeParamEnd = type.end();
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
