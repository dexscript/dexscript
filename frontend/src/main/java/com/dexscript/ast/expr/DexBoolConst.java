package com.dexscript.ast.expr;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.*;

public class DexBoolConst extends DexLeafExpr {

    private Text matched;

    public DexBoolConst(Text src) {
        super(src);
        new Parser();
    }

    public DexBoolConst(String src) {
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

    private class Parser {

        int boolConstBegin = -1;
        int i = src.begin;

        Parser() {
            State.Play(this::trueOrFalse);
        }


        @Expect("true")
        @Expect("false")
        State trueOrFalse() {
            for (;i<src.end;i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 't', 'r', 'u', 'e')) {
                    boolConstBegin = i;
                    i += 4;
                    return this::separator;
                }
                if (Keyword.$(src, i, 'f', 'a', 'l', 's', 'e')) {
                    boolConstBegin = i;
                    i += 5;
                    return this::separator;
                }
                return null;
            }
            return null;
        }

        @Expect("separator")
        State separator() {
            if (Separator.$(src, i)) {
                matched = new Text(src.bytes, boolConstBegin, i);
            }
            return null;
        }
    }
}
