package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.Keyword;

import java.util.ArrayList;
import java.util.List;

public class DexShortVarDecl implements DexStatement {

    private final Text src;
    private List<DexIdentifier> decls;
    private DexExpr expr;
    private DexError err;

    public DexShortVarDecl(Text src) {
        this.src = src;
        new Parser();
    }

    public DexShortVarDecl(String src) {
        this(new Text(src));
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        if (decls == null) {
            throw new IllegalStateException();
        }
        if (decls.size() == 0) {
            throw new IllegalStateException();
        }
        return decls.get(0).begin();
    }

    @Override
    public int end() {
        if (expr == null) {
            throw new IllegalStateException();
        }
        return expr.end();
    }

    @Override
    public boolean matched() {
        return decls != null;
    }

    @Override
    public DexError err() {
        return err;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexExpr expr() {
        return expr;
    }

    public List<DexIdentifier> decls() {
        return decls;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::firstIdentifier);
        }

        @Expect("identifier")
        State firstIdentifier() {
            DexIdentifier identifier = new DexIdentifier(src);
            if (identifier.matched()) {
                i = identifier.end();
                decls = new ArrayList<>();
                decls.add(identifier);
                return this::commaOrColonEqual;
            }
            return null;
        }

        @Expect(",")
        @Expect(":=")
        State commaOrColonEqual() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::moreIdentifiers;
                }
                if (Keyword.__(src, i, ':', '=')) {
                    i += 2;
                    return this::expr;
                }
                break;
            }
            decls = null;
            return null;
        }

        @Expect("identifier")
        State moreIdentifiers() {
            DexIdentifier identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.matched()) {
                i = identifier.end();
                decls.add(identifier);
                return this::commaOrColonEqual;
            }
            decls = null;
            return null;
        }

        State expr() {
            expr = DexExpr.parse(new Text(src.bytes, i, src.end), 0);
            return null;
        }
    }
}
