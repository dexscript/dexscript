package com.dexscript.parser2;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.stmt.DexBlock;
import com.dexscript.parser2.stmt.DexStatement;

public class DexFunctionBody implements DexElement {

    private final Text matched;
    private DexSignature signature;
    private DexBlock block;
    private DexError err;
    private DexFunction parent;

    public DexFunctionBody(Text src) {
        DexFunction nextFunction = new DexFunction(src);
        if (nextFunction.matched()) {
            matched = new Text(src.bytes, src.begin, nextFunction.begin());
        } else {
            matched = src;
        }
    }

    @Override
    public Text src() {
        return matched;
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
        if (signature() != null) {
            signature().reparent(this);
        }
        if (block() != null) {
            block().reparent(this, null);
        }
    }

    @Override
    public DexElement parent() {
        return parent;
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

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexSignature signature() {
        if (signature == null) {
            signature = new DexSignature(matched);
            if (!signature.matched()) {
                err = new DexError(matched, matched.begin);
            }
        }
        return signature;
    }

    public DexBlock block() {
        if (block == null) {
            block = new DexBlock(new Text(matched.bytes, signature().end(), matched.end));
            if (!block.matched()) {
                err = new DexError(matched, signature().end());
            }
        }
        return block;
    }
}
