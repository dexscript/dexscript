package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;

public class TranslateStringLiteral implements Translate {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexStringLiteral iStringLiteral = (DexStringLiteral) iElem;
        iStringLiteral.attach(new OutValue("\"" + iStringLiteral.literalValue() + "\""));
    }
}
