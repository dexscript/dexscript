package com.dexscript.ast.expr;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.One2Nine;
import com.dexscript.ast.token.Separator;
import com.dexscript.ast.token.Zero2Nine;

public class DexIntegerConst extends DexLeafExpr {

    private Text matched;

    public DexIntegerConst(Text src) {
        super(src);
        new Parser();
    }

    public static DexIntegerConst $(String src) {
        return new DexIntegerConst(new Text(src));
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

    private class Parser {

        int i = src.begin;
        int integerBegin = -1;

        Parser() {
            State.Play(this::firstChar);
        }

        @Expect("0~9")
        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '0') {
                    integerBegin = i;
                    i += 1;
                    return this::separator;
                }
                if (One2Nine.$(b)) {
                    integerBegin = i;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        @Expect("0~9")
        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                break;
            }
            return this::separator;
        }

        @Expect("separator")
        State separator() {
            if (Separator.$(src, i)) {
                matched = new Text(src.bytes, integerBegin, i);
            }
            return null;
        }
    }
}
