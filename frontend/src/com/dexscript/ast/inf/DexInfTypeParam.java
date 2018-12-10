package com.dexscript.ast.inf;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.type.DexType;

public class DexInfTypeParam extends DexInfMember {

    private DexIdentifier identifier;
    private DexType type;
    private int typeArgEnd = -1;

    public DexInfTypeParam(Text src) {
        super(src);
        new Parser();
    }

    public DexInfTypeParam(String src) {
        this(new Text(src));
    }

    @Override
    public int end() {
        if (typeArgEnd == -1) {
            throw new IllegalStateException();
        }
        return typeArgEnd;
    }

    @Override
    public boolean matched() {
        return typeArgEnd != -1;
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
            typeArgEnd = type.end();
            return null;
        }

        State missingType() {
            return null;
        }

        State missingColon() {
            throw new UnsupportedOperationException("not implemented");
        }

        State missingRightAngleBracket() {
            throw new UnsupportedOperationException("not implemented");
        }

        State missingIdentifier() {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
