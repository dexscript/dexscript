package com.dexscript.ast.inf;

import com.dexscript.ast.DexInterfaceBody;
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
        if (identifier.matched()){
            identifier.reparent(this);
            sig = new DexSig(src.slice(identifier.begin()));
            sig.reparent(this);
        }
    }

    public DexInfMethod(String src) {
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
