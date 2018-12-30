package com.dexscript.ast.type;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexType extends DexElement {

    public DexType(Text src) {
        super(src);
    }

    public abstract int leftRank();

    public static DexType $parse(String src) {
        DexType elem = parse(new Text(src));
        elem.attach(DexPackage.DUMMY);
        return elem;
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

    private static DexType parseLeft(Text src) {
        DexType type = new DexStringLiteralType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexIntegerLiteralType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexBoolLiteralType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexVoidType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexInterfaceType(src);
        if (type.matched()) {
            return type;
        }
        return new DexTypeRef(src);
    }

    private static DexType parseRight(Text src, DexType left) {
        src = new Text(src.bytes, left.end(), src.end);
        DexType type = new DexParameterizedType(src, left);
        if (type.matched()) {
            return type;
        }
        type = new DexIntersectionType(src, left);
        if (type.matched()) {
            return type;
        }
        type = new DexUnionType(src, left);
        if (type.matched()) {
            return type;
        }
        return type;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }

    public DexParameterizedType asParameterizedType() {
        return (DexParameterizedType) this;
    }
}
