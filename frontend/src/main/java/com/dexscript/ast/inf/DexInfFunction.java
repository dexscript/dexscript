package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexInfFunction extends DexElement {

    private DexIdentifier identifier;
    private DexSig sig;

    public DexInfFunction(Text src) {
        super(src);
        int i = src.begin;
        Text matched = null;
        for (; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (Keyword.$(src, i, ':', ':')) {
                matched = src.slice(i + 2);
                break;
            }
            return;
        }
        if (matched == null) {
            return;
        }
        identifier = new DexIdentifier(matched);
        if (identifier.matched()) {
            identifier.reparent(this);
            sig = new DexSig(matched.slice(identifier.begin()));
            sig.reparent(this);
        }
    }

    public DexInfFunction(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return identifier.begin();
    }

    @Override
    public int end() {
        return sig.end();
    }

    @Override
    public boolean matched() {
        return sig != null && sig.matched();
    }

    @Override
    public void walkDown(DexElement.Visitor visitor) {
        visitor.visit(identifier);
        visitor.visit(sig);
    }

    public DexSig sig() {
        return sig;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }
}
