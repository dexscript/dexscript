package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexBlock;

public class DexInterfaceBody extends DexElement {

    private final Text matched;
    private DexSig signature;
    private DexBlock block;
    private DexSyntaxError syntaxError;

    public DexInterfaceBody(Text src) {
        super(src);
        DexRootDecl nextRootDecl = new DexRootDecl(src);
        if (nextRootDecl.matched()) {
            matched = new Text(src.bytes, src.begin, nextRootDecl.begin());
        } else {
            matched = src;
        }
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return true;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public void reparent(DexFunction parent) {
        this.parent = parent;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }
}
