package com.dexscript.parser2;

import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.read.A2Z;
import com.dexscript.parser2.read.Blank;
import com.dexscript.parser2.read.DecDigit;

public class DexIdentifier implements DexElement {

    private final Text src;
    private DexError err;
    private Text matched;

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
        return err;
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
                return reportError();
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
                if (A2Z.__(b) || DecDigit.__(b) || b == '_') {
                    continue;
                }
                if (Blank.__(b) || b == '(') {
                    matched = new Text(src.bytes, identifierBegin, i);
                    return null;
                }
                return reportError();
            }
            matched = new Text(src.bytes, identifierBegin, src.end);
            return null;
        }

        State reportError() {
            if (err != null) {
                return null;
            }
            err = new DexError(src, i);
            return null;
        }
    }
}
