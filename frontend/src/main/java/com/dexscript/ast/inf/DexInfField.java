package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public class DexInfField extends DexElement {

    public DexInfField(Text src) {
        super(src);
    }

    public static DexInfField $(String src) {
        return new DexInfField(new Text(src));
    }

    @Override
    public int end() {
        return 0;
    }

    @Override
    public boolean matched() {
        return false;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }
}
