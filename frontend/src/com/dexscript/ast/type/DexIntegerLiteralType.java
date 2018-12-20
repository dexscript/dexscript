package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexIntegerConst;

public class DexIntegerLiteralType extends DexType {

    private Text matched;

    public DexIntegerLiteralType(Text src) {
        super(src);
        DexIntegerConst integerLiteral = new DexIntegerConst(src);
        if (integerLiteral.matched()) {
            matched = src.slice(integerLiteral.begin(), integerLiteral.end());
        }
    }

    public DexIntegerLiteralType(String src) {
        this(new Text(src));
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
