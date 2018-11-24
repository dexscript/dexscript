package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexBlock;

public class DexFunctionBody extends DexElement {

    private final Text matched;
    private DexSig signature;
    private DexBlock block;
    private DexError err;

    public DexFunctionBody(Text src) {
        super(src);
        DexFunction nextFunction = new DexFunction(src);
        if (nextFunction.matched()) {
            matched = new Text(src.bytes, src.begin, nextFunction.begin());
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
    public DexError err() {
        return err;
    }

    public void reparent(DexFunction parent) {
        this.parent = parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (signature() != null) {
            visitor.visit(signature());
        }
        if (block() != null) {
            visitor.visit(block());
        }
    }

    public DexSig signature() {
        if (signature == null) {
            signature = new DexSig(matched);
            if (!signature.matched()) {
                err = new DexError(matched, matched.begin);
            } else {
                signature.reparent(this);
            }
        }
        return signature;
    }

    public DexBlock block() {
        if (block == null) {
            block = new DexBlock(new Text(matched.bytes, signature().end(), matched.end));
            if (!block.matched()) {
                err = new DexError(matched, signature().end());
            } else {
                block.reparent(this, null);
            }
        }
        return block;
    }
}
