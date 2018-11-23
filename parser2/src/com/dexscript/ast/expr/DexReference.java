package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.A2Z;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Zero2Nine;

public class DexReference extends DexLeafExpr {

    private DexError err;
    private Text matched;

    public DexReference(String src) {
        this(new Text(src));
    }

    public DexReference(Text src) {
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

    public DexError err() {
        return err;
    }

    @Override
    public int leftRank() {
        return 0;
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
                if (A2Z.__(b) || Zero2Nine.__(b) || b == '_') {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, identifierBegin, i);
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
