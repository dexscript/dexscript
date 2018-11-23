package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public final class DexFunction extends DexRootDecl {

    private DexError err;
    private int functionBegin = -1;
    private int signatureBegin = -1;
    private DexIdentifier identifier;
    private DexFunctionBody body;

    public DexFunction(String src) {
        this(new Text(src));
    }

    public DexFunction(Text src) {
        super(src);
        new Parser();
    }

    public boolean matched() {
        return identifier != null;
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

    public DexSignature sig() {
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
        body.reparent(this);
        return body;
    }

    public DexError err() {
        return err;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(body());
    }

    public void reparent(DexFile parent) {
        this.parent = parent;
    }

    public DexFile file() {
        return (DexFile) parent();
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
                if (b == 'f' && Keyword.__(src, i + 1,
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
