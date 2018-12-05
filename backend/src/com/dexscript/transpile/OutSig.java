package com.dexscript.transpile;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class OutSig {

    private final Gen g = new Gen();

    public OutSig(TypeSystem ts, DexSig iSig, boolean hasType) {
        g.__('(');
        for (int i = 0; i < iSig.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = iSig.params().get(i);
            if (hasType) {
                Type type = InferType.inferType(ts, param.paramType());
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
