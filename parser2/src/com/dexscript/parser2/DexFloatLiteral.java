package com.dexscript.parser2;

import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.MatchKeyword;
import com.dexscript.parser2.token.One2Nine;
import com.dexscript.parser2.token.Zero2Nine;

public class DexFloatLiteral implements DexExpr {

    private final Text src;
    private Text matched;
    private DexError err;

    public DexFloatLiteral(Text src) {
        this.src = src;
        new Parser();
    }

    public DexFloatLiteral(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public Text src() {
        return src;
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
        return null;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;
        int integerBegin = -1;

        Parser() {
            State.Play(this::firstChar);
        }

        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if ('0' == b) {
                    if (MatchKeyword.__(src, i+1, '.')) {
                        integerBegin = i;
                        i += 2;
                        return this::dotFound;
                    }
                    matched = new Text(src.bytes, i, i+1);
                    return null;
                }
                if (One2Nine.__(b)) {
                    integerBegin = i;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.__(b)) {
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
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }

        State dotFound() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.__(b)) {
                    continue;
                }
                if (b == 'e' || b == 'E') {
                    i += 1;
                    return this::eFound;
                }
                break;
            }
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }

        State eFound() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Zero2Nine.__(b)) {
                    continue;
                }
                if (b == '+' || b == '-') {
                    i += 1;
                    return this::plusOrMinusFound;
                }
                break;
            }
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }

        State plusOrMinusFound() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Zero2Nine.__(b)) {
                    continue;
                }
                break;
            }
            if (j == 0) {
                return reportError();
            }
            matched = new Text(src.bytes, integerBegin, i);
            return null;
        }

        State reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
            return null;
        }
    }
}
