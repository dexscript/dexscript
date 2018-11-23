package com.dexscript.parser2.expr;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.stmt.DexStatement;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.Keyword;
import com.dexscript.parser2.token.One2Nine;
import com.dexscript.parser2.token.Zero2Nine;

public class DexFloatLiteral implements DexLeafExpr {

    private final Text src;
    private Text matched;
    private DexError err;

    // for walk up
    private DexElement parent;
    private DexStatement stmt;

    public DexFloatLiteral(Text src) {
        this.src = src;
        new Parser();
    }

    public DexFloatLiteral(String src) {
        this(new Text(src));
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public DexStatement stmt() {
        return stmt;
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
        return err;
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
                    if (Keyword.__(src, i+1, '.')) {
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
            matched = new Text(src.bytes, integerBegin, i);
            if (j == 0) {
                return reportError();
            }
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
