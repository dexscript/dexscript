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

        State leftAngleBracket() {
            return null;
        }

        @Expect("paramName")
        State paramName() {
            paramName = new DexIdentifier(new Text(src.bytes, i, src.end));
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

        @Expect("reference")
        State paramType() {
            paramType = DexType.parse(src.slice(i));
            if (paramType.matched()) {
                return null;
            }
            return null;
        }
    }
}
