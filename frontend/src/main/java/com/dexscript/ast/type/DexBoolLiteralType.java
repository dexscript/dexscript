package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexBoolConst;

public class DexBoolLiteralType extends DexType {

    private Text matched;

    public DexBoolLiteralType(Text src) {
        super(src);
        DexBoolConst boolConst = new DexBoolConst(src);
        if (boolConst.matched()) {
            matched = src.slice(boolConst.begin(), boolConst.end());
        }
    }

    public static DexBoolLiteralType $(String src) {
        return new DexBoolLiteralType(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    @Override
    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return matched != null;
    }

    @Override
    public void walkDown(Visitor visitor) {
    }
}
