package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexIdentifier;
import com.dexscript.ast.token.Blank;

public class DexParam implements DexElement {

    private final Text src;
    private DexIdentifier paramName;
    private DexReference paramType;

    // for walk up
    private DexElement parent;

    public DexParam(Text src) {
        this.src = src;
        new Parser();
    }

    public DexIdentifier paramName() {
        return paramName;
    }

    public DexReference paramType() {
        return paramType;
    }

    @Override
    public Text src() {
        return src;
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
        if (paramType != null && paramType.matched()) {
            return paramType.end();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean matched() {
        return paramType != null && paramType.matched();
    }

    @Override
    public DexError err() {
        return null;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
        if (paramName() != null) {
            paramName().reparent(this);
        }
        if (paramType() != null) {
            paramType().reparent(this, null);
        }
    }

    @Override
    public DexElement parent() {
        return parent;
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
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::paramName);
        }

        @Expect("identifier")
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
                if (Blank.__(b)) {
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
            paramType = new DexReference(new Text(src.bytes, i, src.end));
            if (paramType.matched()) {
                return null;
            }
            return null;
        }
    }
}
