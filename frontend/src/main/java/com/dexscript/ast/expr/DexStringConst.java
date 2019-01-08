package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexStringConst extends DexLeafExpr {

    private Text matched;
    private String constVal;
    private DexSyntaxError syntaxError;

    public DexStringConst(Text src) {
        super(src);
        new Parser();
    }

    public static DexStringConst $(String src) {
        return new DexStringConst(new Text(src));
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

    public String constValue() {
        return constVal;
    }

    private class Parser {

        int i = src.begin;
        int strBegin = -1;
        byte[] valBuffer;
        int valLen = 0;

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
                    byte nextByte = src.bytes[i];
                    switch (nextByte) {
                        case '\\':
                            append((byte) '\\');
                            continue;
                        case '\'':
                            append((byte) '\'');
                            continue;
                        case 't':
                            append((byte) '\t');
                            continue;
                        case 'n':
                            append((byte) '\n');
                            continue;
                        case 'r':
                            append((byte) '\r');
                            continue;
                        default:
                            reportError();
                            continue;
                    }
                }
                if (b == '\'') {
                    matched = src.slice(strBegin, i + 1);
                    if (valBuffer == null) {
                        constVal = "";
                    } else {
                        constVal = new String(valBuffer, 0, valLen);
                    }
                    return null;
                }
                append(b);
            }
            reportError();
            matched = src.slice(strBegin, i);
            constVal = new String(valBuffer, 0, valLen);
            return null;
        }

        void append(byte b) {
            if (valBuffer == null) {
                valBuffer = new byte[16];
            } else if (valLen == valBuffer.length) {
                byte[] newBuffer = new byte[valBuffer.length * 2];
                System.arraycopy(valBuffer, 0, newBuffer, 0, valLen);
                valBuffer = newBuffer;
            }
            valBuffer[valLen++] = b;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
