package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexStringLiteral extends DexLeafExpr {

    private Text matched;
    private DexError err;

    public DexStringLiteral(Text src) {
        super(src);
        new Parser();
    }

    public DexStringLiteral(String src) {
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
    public DexError err() {
        return err;
    }

    private class Parser {

        int i = src.begin;
        int strBegin = -1;

        Parser() {
            State.Play(this::leftQuote);
        }

        @Expect("'")
        State leftQuote() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '\'') {
                    strBegin = i;
                    i += 1;
                    return this::body;
                }
                break;
            }
            return null;
        }

        @Expect("string")
        State body() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == '\\') {
                    i += 1;
                    if (i >= src.end) {
                        break;
                    }
                    continue;
                }
                if (b == '\'') {
                    matched = src.slice(strBegin, i + 1);
                    return null;
                }
            }
            matched = src.slice(strBegin, i);
            return reportError();
        }

        State reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
            return null;
        }
    }
}
