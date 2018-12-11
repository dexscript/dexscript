package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

public class DexVarDecl extends DexStatement {

    private DexSyntaxError syntaxError;
    private DexIdentifier identifier;
    private DexType type;
    private int varDeclEnd = -1;

    public DexVarDecl(Text src) {
        super(src);
        new Parser();
    }

    public DexVarDecl(String src) {
        this(new Text(src));
    }

    @Override
    public int end() {
        if (varDeclEnd == -1) {
            throw new IllegalStateException();
        }
        return varDeclEnd;
    }

    @Override
    public boolean matched() {
        return varDeclEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (identifier() != null) {
            visitor.visit(identifier());
        }
        if (type() != null) {
            visitor.visit(type());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public DexType type() {
        return type;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::varKeyword);
        }

        @Expect("var")
        State varKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'v', 'a', 'r')) {
                    i += 3;
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
            if (j == 0) {
                return null;
            }
            return this::identifier;
        }

        @Expect("paramName")
        State identifier() {
            identifier = new DexIdentifier(src.slice(i));
            identifier.reparent(DexVarDecl.this);
            if (!identifier.matched()) {
                return this::missingIdentifier;
            }
            i = identifier.end();
            return this::colon;
        }

        private State colon() {
            for (;i<src.end;i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::type;
                }
                return this::missingColon;
            }
            return this::missingColon;
        }

        State missingColon() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    varDeclEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::type;
                }
            }
            varDeclEnd = i;
            return null;
        }

        @Expect("type")
        State type() {
            type = DexType.parse(src.slice(i));
            type.reparent(DexVarDecl.this);
            if (!type.matched()) {
                return this::missingType;
            }
            varDeclEnd = type.end();
            return null;
        }

        State missingType() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    varDeclEnd = i;
                    return null;
                }
            }
            varDeclEnd = i;
            return null;
        }

        State missingIdentifier() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    varDeclEnd = i;
                    return null;
                }
                if (b == ':') {
                    i += 1;
                    return this::type;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::colon;
                }
            }
            varDeclEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
