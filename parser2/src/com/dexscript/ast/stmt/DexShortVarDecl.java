package com.dexscript.ast.stmt;

import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

import java.util.ArrayList;
import java.util.List;

public class DexShortVarDecl implements DexStatement {

    private final Text src;
    private List<DexIdentifier> decls;
    private DexExpr expr;
    private DexError err;

    // for walk up
    private DexElement parent;
    private DexStatement prev;

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
    public DexElement parent() {
        return parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (decls() != null) {
            for (DexIdentifier decl : decls()) {
                visitor.visit(decl);
            }
        }
        if (expr() != null) {
            visitor.visit(expr());
        }
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

    @Override
    public void reparent(DexElement parent, DexStatement prev) {
        this.parent = parent;
        this.prev = prev;
        if (decls() != null) {
            for (DexIdentifier decl : decls()) {
                decl.reparent(this);
            }
        }
        if (expr() != null) {
            expr().reparent(this, this);
        }
    }

    @Override
    public DexStatement prev() {
        return prev;
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
            // TODO: handle error
            return null;
        }
    }
}
