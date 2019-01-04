package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexTopLevelDecl extends DexElement {

    private DexElement matched;
    private DexSyntaxError syntaxError;

    public DexTopLevelDecl(Text src) {
        super(src);
        new Parser();
    }

    public static DexTopLevelDecl $(String src) {
        return new DexTopLevelDecl(new Text(src));
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

    public DexActor actor() {
        if (matched instanceof DexActor) {
            return (DexActor) matched;
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
        if (actor() != null) {
            actor().reparent(parent);
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
                if (Blank.$(b)) {
                    continue;
                }
                matched = new DexActor(src.slice(i));
                if (matched.matched()) {
                    return null;
                }
                matched = new DexInterface(src.slice(i));
                if (matched.matched()) {
                    return null;
                }
                return reportError();
            }
            return null;
        }

        @Expect("blank")
        State skipError() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
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
