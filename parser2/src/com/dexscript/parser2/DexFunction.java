package com.dexscript.parser2;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.stmt.DexBlock;
import com.dexscript.parser2.stmt.DexIdentifier;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.MatchKeyword;

public class DexFunction implements DexElement {

    private final Text src;
    private DexError err;
    private int functionBegin = -1;
    private int signatureBegin = -1;
    private DexIdentifier identifier;
    private DexFunctionBody body;

    public DexFunction(String src) {
        this(new Text(src));
    }

    public DexFunction(Text src) {
        this.src = src;
        new Parser();
    }

    public boolean matched() {
        return identifier != null;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        if (functionBegin == -1) {
            throw new IllegalStateException();
        }
        return functionBegin;
    }

    @Override
    public int end() {
        return body().end();
    }

    public DexIdentifier identifier() {
        if (identifier == null) {
            throw new IllegalStateException();
        }
        return identifier;
    }

    public DexSignature signature() {
        return body().signature();
    }

    public DexBlock block() {
        return body().block();
    }

    public DexFunctionBody body() {
        if (body != null) {
            return body;
        }
        body = new DexFunctionBody(new Text(src.bytes, signatureBegin, src.end));
        return body;
    }

    public DexError err() {
        return err;
    }

    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i;

        Parser() {
            i = src.begin;
            State.Play(this::function);
        }

        @Expect("function")
        State function() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == 'f' && MatchKeyword.__(src, i + 1,
                        'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    functionBegin = i;
                    i = i + 8;
                    return this::identifier;
                }
                return reportError();
            }
            return null;
        }

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State identifier() {
            identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.matched()) {
                i = identifier.end();
                return this::leftParen;
            }
            return reportError();
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                if (!Blank.__(src.bytes[i])) {
                    break;
                }
            }
            if (src.bytes[i] != '(') {
                return reportError();
            }
            signatureBegin = i;
            return null;
        }

        @Expect("blank")
        State skipError() {
            functionBegin = -1;
            identifier = null;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    return this::function;
                }
            }
            return null;
        }

        State reportError() {
            if (err != null) {
                return this::skipError;
            }
            err = new DexError(src, i);
            return this::skipError;
        }
    }
}
