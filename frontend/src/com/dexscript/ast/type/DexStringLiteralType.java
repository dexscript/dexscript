package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexStringConst;

public class DexStringLiteralType extends DexType {

    private Text matched;

    public DexStringLiteralType(Text src) {
        super(src);
        DexStringConst stringLiteral = new DexStringConst(src);
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

    public String literalValue() {
        return matched.slice(matched.begin + 1, matched.end - 1).toString();
    }
}
