package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexFloatLiteral;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexRootDecl extends DexElement {

    private DexElement matched;
    private DexSyntaxError syntaxError;

    public DexRootDecl(Text src) {
        super(src);
        new Parser();
    }

    public DexRootDecl(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return matched.begin();
    }

    @Override
    public int end() {
        return matched.end();
    }

    @Override
    public boolean matched() {
        return matched != null && matched.matched();
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(matched);
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexFunction function() {
        if (matched instanceof DexFunction) {
            return (DexFunction) matched;
        }
        return null;
    }

    public DexInterface inf() {
        if (matched instanceof DexInterface) {
            return (DexInterface) matched;
        }
        return null;
    }

    public void reparent(DexFile parent) {
        this.parent = parent;
        if (function() != null) {
            function().reparent(parent);
        } else if (inf() != null) {
            inf().reparent(parent);
        }
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::functionOrInterface);
        }

        State functionOrInterface() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i, 'f', 'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    matched = new DexFunction(src.slice(i));
                    if (matched.matched()) {
                        return null;
                    } else {
                        i += 8;
                        return this::skipError;
                    }
                }
                if (Keyword.__(src, i,
                        'i', 'n', 't', 'e', 'r', 'f', 'a', 'c', 'e')) {
                    matched = new DexInterface(src.slice(i));
                    if (matched.matched()) {
                        return null;
                    } else {
                        i += 9;
                        return this::skipError;
                    }
                }
                return reportError();
            }
            return null;
        }

        @Expect("blank")
        State skipError() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    return this::functionOrInterface;
                }
            }
            return null;
        }

        State reportError() {
            if (syntaxError != null) {
                return this::skipError;
            }
            syntaxError = new DexSyntaxError(src, i);
            return this::skipError;
        }
    }
}
