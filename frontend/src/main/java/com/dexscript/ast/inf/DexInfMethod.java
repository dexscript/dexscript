package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;

public class DexInfMethod extends DexElement {

    private String infTypeName;
    private DexIdentifier identifier;
    private DexSig sig;
    private DexSig asFuncSig;

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

    public void reparent(DexElement parent, String infTypeName) {
        this.parent = parent;
        this.infTypeName = infTypeName;
    }

    public DexSig asFuncSig() {
        if (asFuncSig != null) {
            return asFuncSig;
        }
        StringBuilder sig = new StringBuilder("(");
        boolean isFirst = true;
        for (DexTypeParam typeParam : sig().typeParams()) {
            isFirst = appendMore(sig, isFirst);
            sig.append(typeParam.toString());
        }
        appendMore(sig, isFirst);
        sig.append("self: ");
        sig.append(infTypeName);
        for (DexParam param : sig().params()) {
            sig.append(", ");
            sig.append(param.toString());
        }
        sig.append("): ");
        sig.append(sig().ret().toString());
        asFuncSig = new DexSig(new Text(sig.toString()));
        asFuncSig.reparent(this);
        return asFuncSig;
    }

    private static boolean appendMore(StringBuilder sig, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
        } else {
            sig.append(", ");
        }
        return isFirst;
    }
}
