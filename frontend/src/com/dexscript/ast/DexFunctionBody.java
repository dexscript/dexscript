package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.stmt.DexBlock;

public class DexFunctionBody extends DexElement {

    private final Text matched;
    private DexSig sig;
    private DexBlock blk;
    private DexSyntaxError syntaxError;

    public DexFunctionBody(Text src) {
        super(src);
        DexTopLevelDecl nextRootDecl = new DexTopLevelDecl(src);
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

    public void reparent(DexActor parent) {
        this.parent = parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (sig() != null) {
            visitor.visit(sig());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
    }

    public DexSig sig() {
        if (sig == null) {
            sig = new DexSig(matched);
            if (!sig.matched()) {
                syntaxError = new DexSyntaxError(matched, matched.begin);
            } else {
                sig.reparent(this);
            }
        }
        return sig;
    }

    public DexBlock blk() {
        if (blk == null) {
            blk = new DexBlock(new Text(matched.bytes, sig().end(), matched.end));
            if (!blk.matched()) {
                syntaxError = new DexSyntaxError(matched, sig().end());
            } else {
                blk.reparent(this, null);
            }
        }
        return blk;
    }
}
