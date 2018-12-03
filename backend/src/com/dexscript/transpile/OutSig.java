package com.dexscript.transpile;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.denotation.Type;
import com.dexscript.transpile.gen.Gen;

public class OutSig {

    private final Gen g = new Gen();

    public OutSig(Town town, DexSig iSig, boolean hasType) {
        g.__('(');
        for (int i = 0; i < iSig.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = iSig.params().get(i);
            if (hasType) {
                Type type = town.resolveType(param.paramType());
                g.__(type.javaClassName());
            } else {
                g.__("Object");
            }
            g.__(' '
            ).__(param.paramName().toString());
        }
        g.__(')');
    }

    @Override
    public String toString() {
        return g.toString();
    }
}
