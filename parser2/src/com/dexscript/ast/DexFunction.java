package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexIdentifier;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

import java.util.List;

public final class DexFunction extends DexElement {

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
        return signatureBegin != -1;
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

    public DexSig sig() {
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

    public DexSyntaxError syntaxError() {
        return null;
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

    public List<DexStatement> stmts() {
        return block().stmts();
    }

    private class Parser {

        int i;

        Parser() {
            i = src.begin;
            State.Play(this::functionKeyword);
        }

        @Expect("function")
        State functionKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i,
                        'f', 'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    functionBegin = i;
                    i = i + 8;
                    return this::blank;
                }
                return null;
            }
            return null;
        }

        @Expect("blank")
        State blank() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                if (Blank.__(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return null;
        }

        @Expect("identifier")
        State identifier() {
            identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.matched()) {
                i = identifier.end();
                return this::leftParen;
            }
            return null;
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                if (!Blank.__(src.bytes[i])) {
                    break;
                }
            }
            if (src.bytes[i] != '(') {
                return null;
            }
            // matched
            signatureBegin = i;
            return null;
        }
    }
}
