package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

public final class DexPackageClause extends DexElement {

    private int packageBegin = -1;
    private int packageEnd = -1;
    private DexIdentifier identifier;
    private DexSyntaxError syntaxError;

    public DexPackageClause(Text src) {
        super(src);
        new Parser();
    }

    public DexPackageClause(String src) {
        this(new Text(src));
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    @Override
    public int begin() {
        if (packageBegin == -1) {
            throw new IllegalStateException();
        }
        return packageBegin;
    }

    @Override
    public int end() {
        if (packageEnd == -1) {
            throw new IllegalStateException();
        }
        return packageEnd;
    }

    @Override
    public boolean matched() {
        return packageEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }


    public void reparent(DexFile parent) {
        this.parent = parent;
        if (identifier() != null) {
            identifier().reparent(this);
        }
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (identifier() != null) {
            visitor.visit(identifier());
        }
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::packageKeyword);
        }

        @Expect("package")
        State packageKeyword() {
            for (int j = 0; i < src.end; i++, j++) {
                if (Blank.$(src.bytes[i])) {
                    continue;
                }
                boolean hasSpaceInPrelude = i == 0 || j > 0;
                boolean matchPackage = Keyword.$(src, i, 'p', 'a', 'c', 'k', 'a', 'g', 'e');
                if (hasSpaceInPrelude && matchPackage) {
                    packageBegin = i;
                    i += 7;
                    return this::blank;
                }
                i += 1;
                return reportError();
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
            return reportError();
        }

        @Expect("paramName")
        State identifier() {
            identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.matched()) {
                packageEnd = identifier.end();
            } else {
                return this::missingIdentifier;
            }
            return null;
        }

        private State missingIdentifier() {
            reportError();
            for (; i < src.end; i++) {
                if (LineEnd.$(src.bytes[i])) {
                    packageEnd = i;
                    return null;
                }
            }
            packageEnd = i;
            return null;
        }

        State reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            return this::packageKeyword;
        }
    }
}
