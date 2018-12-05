package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexValueRef;

public class DexTypeRef extends DexType {

    private Text matched;

    public DexTypeRef(Text src) {
        super(src);
        DexValueRef valueRef = new DexValueRef(src);
        if (valueRef.matched()) {
            matched = src.slice(valueRef.begin(), valueRef.end());
        }
    }

    public DexTypeRef(String src) {
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
