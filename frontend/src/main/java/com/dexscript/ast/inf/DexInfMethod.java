package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexSig;

public class DexInfMethod extends DexElement {

    private DexIdentifier identifier;
    private DexSig sig;

    public DexInfMethod(Text src) {
        super(src);
        identifier = new DexIdentifier(src);
        identifier.reparent(this);
        if (identifier.matched()){
            sig = new DexSig(src.slice(identifier.end()));
            sig.reparent(this);
        }
    }

    public static DexInfMethod $(String src) {
        return new DexInfMethod(new Text(src));
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
    public void walkDown(Visitor visitor) {
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
