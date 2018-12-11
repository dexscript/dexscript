package com.dexscript.ast.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.type.DexType;

public class DexTypeParam extends DexElement {

    private DexIdentifier paramName;
    private DexType paramType;
    private int typeParamEnd = -1;

    public DexTypeParam(Text src) {
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
            throw new UnsupportedOperationException("not implemented");
        }

        State missingRightAngleBracket() {
            throw new UnsupportedOperationException("not implemented");
        }

        State missingParamName() {
            throw new UnsupportedOperationException("not implemented");
        }

        State missingColon() {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
