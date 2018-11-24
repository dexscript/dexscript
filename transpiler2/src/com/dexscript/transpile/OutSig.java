package com.dexscript.transpile;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexSig;
import com.dexscript.resolve.Denotation;
import com.dexscript.transpile.gen.Gen;

public class OutSig {

    private final Gen g = new Gen();

    public OutSig(Town town, DexSig iSig) {
        g.__('(');
        for (int i = 0; i < iSig.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = iSig.params().get(i);
            Denotation.Type type = town.resolveType(param.paramType());
            g.__(type.javaClassName
            ).__(' '
            ).__(param.paramName().toString());
        }
        g.__(')');
    }

    @Override
    public String toString() {
        return g.toString();
    }
}
