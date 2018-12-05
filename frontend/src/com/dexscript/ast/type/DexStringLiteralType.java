package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexStringLiteral;

public class DexStringLiteralType extends DexType {

    private Text matched;

    public DexStringLiteralType(Text src) {
        super(src);
        DexStringLiteral stringLiteral = new DexStringLiteral(src);
        if (stringLiteral.matched()) {
            matched = src.slice(stringLiteral.begin(), stringLiteral.end());
        }
    }

    public DexStringLiteralType(String src) {
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
