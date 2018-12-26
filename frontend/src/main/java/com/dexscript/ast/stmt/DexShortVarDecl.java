package com.dexscript.ast.stmt;

import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexShortVarDecl extends DexSimpleStatement {

    private List<DexIdentifier> decls;
    private DexExpr expr;
    private int shortVarDeclEnd = -1;
    private DexSyntaxError syntaxError;

    public DexShortVarDecl(Text src) {
        super(src);
        new Parser();
    }

    public static DexShortVarDecl $(String src) {
        return new DexShortVarDecl(new Text(src));
    }

    @Override
    public int end() {
        if (shortVarDeclEnd == -1) {
            throw new IllegalStateException();
        }
        return shortVarDeclEnd;
    }

    @Override
    public boolean matched() {
        return shortVarDeclEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
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

    public DexExpr expr() {
        return expr;
    }

    public List<DexIdentifier> decls() {
        return decls;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::identifier);
        }

        @Expect("paramName")
        State identifier() {
            DexIdentifier identifier = new DexIdentifier(src.slice(i));
            identifier.reparent(DexShortVarDecl.this);
            if (identifier.matched()) {
                i = identifier.end();
                if (decls == null) {
                    decls = new ArrayList<>();
                }
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
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::identifier;
                }
                if (Keyword.$(src, i, ':', '=')) {
                    i += 2;
                    return this::expr;
                }
                break;
            }
            decls = null;
            return null;
        }

        State expr() {
            expr = DexExpr.parse(new Text(src.bytes, i, src.end), 0);
            expr.reparent(DexShortVarDecl.this, DexShortVarDecl.this);
            if (!expr.matched()) {
                return this::missingExpr;
            }
            shortVarDeclEnd = expr.end();
            return null;
        }

        private State missingExpr() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    shortVarDeclEnd = i;
                    return null;
                }
            }
            shortVarDeclEnd = i;
            return null;
        }
    }
}
