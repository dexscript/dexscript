package com.dexscript.ast.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexUnionType extends DexType {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;
    private final DexType left;
    private DexType right;

    public DexUnionType(Text src, DexType left) {
        super(src);
        this.left = left;
        left.reparent(this);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == '|') {
                right = DexType.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                right.reparent(this);
                return;
            }
            return;
        }
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int begin() {
        return left.begin();
    }

    @Override
    public int end() {
        if (!matched()) {
            throw new IllegalStateException();
        }
        return right().end();
    }

    @Override
    public boolean matched() {
        return right != null && right.matched();
    }

    @Override
    public void walkDown(DexElement.Visitor visitor) {
        if (left() != null) {
            visitor.visit(left());
        }
        if (right() != null) {
            visitor.visit(right());
        }
    }

    public DexType left() {
        return left;
    }

    public DexType right() {
        return right;
    }
}
