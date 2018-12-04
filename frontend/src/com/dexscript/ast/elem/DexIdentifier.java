package com.dexscript.ast.elem;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.A2Z;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.token.Zero2Nine;

public final class DexIdentifier extends DexLeafElement {

    private Text matched;

    public DexIdentifier(String src) {
        this(new Text(src));
    }

    public DexIdentifier(Text src) {
        super(src);
        new Parser();
    }

    public boolean matched() {
        return matched != null;
    }

    public int begin() {
        if (matched == null) {
            throw new IllegalStateException();
        }
        return matched.begin;
    }

    public int end() {
        if (matched == null) {
            throw new IllegalStateException();
        }
        return matched.end;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }

    private class Parser {

        int i;
        int identifierBegin;

        Parser() {
            i = src.begin;
            State.Play(this::firstChar);
        }

        @Expect("blank")
        @Expect("a~z")
        @Expect("A~Z")
        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (A2Z.$(b)) {
                    identifierBegin = i;
                    i += 1;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (A2Z.$(b) || Zero2Nine.$(b) || b == '_') {
                    continue;
                }
                if (Blank.$(b) || LineEnd.$(b) || b == '(' || b == '{' || b == ':' || b == ',') {
                    matched = new Text(src.bytes, identifierBegin, i);
                    return null;
                }
                // not matched, found invalid char
                return null;
            }
            matched = new Text(src.bytes, identifierBegin, i);
            return null;
        }
    }
}
