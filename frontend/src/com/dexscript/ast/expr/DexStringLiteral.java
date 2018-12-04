package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public class DexStringLiteral extends DexLeafExpr {

    private Text matched;
    private DexSyntaxError syntaxError;

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
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public String literalValue() {
        return src.slice(matched.begin + 1, matched.end - 1).toString();
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
                if (Blank.$(b)) {
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
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            return null;
        }
    }
}
