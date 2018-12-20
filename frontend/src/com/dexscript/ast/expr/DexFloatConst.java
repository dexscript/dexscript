package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.One2Nine;
import com.dexscript.ast.token.Zero2Nine;

public class DexFloatConst extends DexLeafExpr {

    private Text matched;
    private DexSyntaxError syntaxError;

    public DexFloatConst(Text src) {
        super(src);
        new Parser();
    }

    public DexFloatConst(String src) {
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

    private class Parser {

        int i = src.begin;
        int floatConstBegin = -1;

        Parser() {
            State.Play(this::firstChar);
        }

        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if ('0' == b) {
                    if (Keyword.$(src, i+1, '.')) {
                        floatConstBegin = i;
                        i += 2;
                        return this::dotFound;
                    }
                    matched = new Text(src.bytes, i, i+1);
                    return null;
                }
                if (One2Nine.$(b)) {
                    floatConstBegin = i;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                if ('e' == b) {
                    i += 1;
                    return this::eFound;
                }
                if ('.' == b) {
                    i += 1;
                    return this::dotFound;
                }
                break;
            }
            matched = new Text(src.bytes, floatConstBegin, i);
            return null;
        }

        State dotFound() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                if (b == 'e' || b == 'E') {
                    i += 1;
                    return this::eFound;
                }
                matched = new Text(src.bytes, floatConstBegin, i);
                return null;
            }
            matched = new Text(src.bytes, floatConstBegin, i);
            return null;
        }

        State eFound() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                if (b == '+' || b == '-') {
                    i += 1;
                    return this::plusOrMinusFound;
                }
                break;
            }
            matched = new Text(src.bytes, floatConstBegin, i);
            return null;
        }

        State plusOrMinusFound() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Zero2Nine.$(b)) {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, floatConstBegin, i);
            if (j == 0) {
                return reportError();
            }
            return null;
        }

        State reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            return null;
        }
    }
}
