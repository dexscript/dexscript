package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexFloatConst;
import com.dexscript.ast.expr.DexIntegerConst;

public class DexIntegerLiteralType extends DexType {

    private Text matched;

    public DexIntegerLiteralType(Text src) {
        super(src);
        DexFloatConst floatConst = new DexFloatConst(src);
        if (!floatConst.matched()) {
            return;
        }
        DexIntegerConst integerConst = new DexIntegerConst(src.slice(floatConst.begin(), floatConst.end()));
        if (integerConst.end() == floatConst.end()) {
            matched = src.slice(integerConst.begin(), integerConst.end());
        }
    }

    public static DexIntegerLiteralType $(String src) {
        return new DexIntegerLiteralType(new Text(src));
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
