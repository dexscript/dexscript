package com.dexscript.ast.type;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexArrayType extends DexType {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;
    private final DexType left;
    private int typeArrayEnd = -1;

    public DexArrayType(Text src, DexType left) {
        super(src);
        this.left = left;
        new Parser();
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
        if (typeArrayEnd == -1) {
            throw new IllegalStateException();
        }
        return typeArrayEnd;
    }

    @Override
    public boolean matched() {
        return typeArrayEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (left() != null) {
            visitor.visit(left());
        }
    }

    public DexType left() {
        return left;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftBracket);
        }

        @Expect("[")
        private State leftBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '[') {
                    i += 1;
                    return this::rightBracket;
                }
                return null;
            }
            return null;
        }

        private State rightBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ']') {
                    typeArrayEnd = i + 1;
                    return null;
                }
                return null;
            }
            return null;
        }
    }
}
