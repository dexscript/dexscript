package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.type.DexType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class DexActor extends DexElement {

    private int functionBegin = -1;
    private int signatureBegin = -1;
    private DexIdentifier identifier;
    private DexActorBody body;
    private String actorName;

    public DexActor(Text src) {
        super(src);
        new Parser();
    }

    public static DexActor $(String src) {
        DexActor elem = new DexActor(new Text(src));
        elem.attach(DexPackage.DUMMY);
        return elem;
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
        return body().sig();
    }

    public List<DexTypeParam> typeParams() {
        return sig().typeParams();
    }

    public List<DexParam> params() {
        return sig().params();
    }

    public DexType ret() {
        return sig().ret();
    }

    public DexBlock blk() {
        return body().blk();
    }

    public DexActorBody body() {
        if (body != null) {
            return body;
        }
        body = new DexActorBody(new Text(src.bytes, signatureBegin, src.end));
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
        return blk().stmts();
    }

    public String actorName() {
        if (actorName != null) {
            return actorName;
        }
        return actorName = identifier.toString() + "__" + md5(toString()).substring(0, 4);
    }

    public String functionName() {
        return identifier.toString();
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
                if (Blank.$(b)) {
                    continue;
                }
                // function
                if (Keyword.$(src, i,
                        'f', 'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    functionBegin = i;
                    i = i + 8;
                    return this::blank;
                }
                // actor
                if (Keyword.$(src, i,
                        'a', 'c', 't', 'o', 'r')) {
                    functionBegin = i;
                    i = i + 5;
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
                if (Blank.$(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return null;
        }

        @Expect("paramName")
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
                if (!Blank.$(src.bytes[i])) {
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

    @NotNull
    private static String md5(String mySrc) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(mySrc.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new DexSyntaxException(e);
        }
    }
}
