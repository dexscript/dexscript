package com.dexscript.parser2;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.stmt.DexIdentifier;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.Keyword;
import com.dexscript.parser2.token.LineEnd;

public class DexPackageClause implements DexElement {

    private final Text src;
    private int packageBegin = -1;
    private int packageEnd = -1;
    private DexIdentifier identifier;
    private DexError err;

    public DexPackageClause(Text src) {
        this.src = src;
        new Parser();
    }

    public DexPackageClause(String src) {
        this(new Text(src));
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    @Override
    public Text src() {
        return src;
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
    public DexError err() {
        return err;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(identifier);
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::packageKeyword);
        }

        @Expect("package")
        State packageKeyword() {
            for (int j = 0; i < src.end; i++, j++) {
                if (Blank.__(src.bytes[i])) {
                    continue;
                }
                boolean hasSpaceInPrelude = i == 0 || j > 0;
                boolean matchPackage = Keyword.__(src, i, 'p', 'a', 'c', 'k', 'a', 'g', 'e');
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
                if (Blank.__(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return reportError();
        }

        @Expect("identifier")
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
                if (LineEnd.__(src.bytes[i])) {
                    packageEnd = i;
                    return null;
                }
            }
            packageEnd = i;
            return null;
        }

        State reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
            return this::packageKeyword;
        }
    }
}
