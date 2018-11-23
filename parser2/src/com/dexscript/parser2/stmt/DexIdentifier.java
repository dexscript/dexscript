package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.token.A2Z;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.LineEnd;
import com.dexscript.parser2.token.Zero2Nine;

public class DexIdentifier implements DexLeafElement {

    private final Text src;
    private Text matched;

    // for walk up
    private DexElement parent;

    public DexIdentifier(String src) {
        this(new Text(src));
    }

    public DexIdentifier(Text src) {
        this.src = src;
        new Parser();
    }

    public boolean matched() {
        return matched != null;
    }

    @Override
    public Text src() {
        return src;
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

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexError err() {
        return null;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }

    @Override
    public DexElement parent() {
        return parent;
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
                if (Blank.__(b)) {
                    continue;
                }
                if (A2Z.__(b)) {
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
                if (A2Z.__(b) || Zero2Nine.__(b) || b == '_') {
                    continue;
                }
                if (Blank.__(b) || LineEnd.__(b) || b == '(' || b == ':' || b == ',') {
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
