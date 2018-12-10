package com.dexscript.ast.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexType extends DexElement {

    public DexType(Text src) {
        super(src);
    }

    public abstract int leftRank();

    public static DexType parse(String src) {
        return parse(new Text(src));
    }

    public static DexType parse(Text src) {
        return parse(src, 0);
    }

    public static DexType parse(Text src, int rightRank) {
        DexType left = parseLeft(src);
        if (!left.matched()) {
            return left;
        }
        while (true) {
            DexType expr = parseRight(src, left);
            if (!expr.matched()) {
                return left;
            }
            if (rightRank >= expr.leftRank()) {
                return left;
            }
            left = expr;
        }
    }

    public static DexType parseLeft(Text src) {
        DexType type = new DexStringLiteralType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexVoidType(src);
        if (type.matched()) {
            return type;
        }
        return new DexTypeRef(src);
    }

    public static DexType parseRight(Text src, DexType left) {
        src = new Text(src.bytes, left.end(), src.end);
        return new DexGenericType(src, left);
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }
}
