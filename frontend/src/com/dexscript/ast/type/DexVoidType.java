package com.dexscript.ast.type;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.Separator;

public class DexVoidType extends DexType {

    private boolean matched;

    public DexVoidType(Text src, boolean matched) {
        super(src);
        this.matched = matched;
    }

    public DexVoidType(Text src) {
        super(src);
        new Parser();
    }

    public DexVoidType(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return src.begin;
    }

    @Override
    public boolean matched() {
        return matched;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    @Override
    public String toString() {
        if (matched()) {
            return "void";
        }
        return super.toString();
    }

    private class Parser {
        int i = src.begin;

        Parser() {
            State.Play(this::voidKeyword);
        }

        @Expect("void")
        State voidKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'v', 'o', 'i', 'd')) {
                    i += 4;
                    return this::separator;
                }
                return null;
            }
            return null;
        }

        State separator() {
            if (Separator.$(src, i)) {
                matched = true;
            }
            return null;
        }
    }
}
