package com.dexscript.parser2;

import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;

import java.util.Arrays;
import java.util.List;

public class DexRootDeclaration {

    private enum State {
        PRE_BLANK,
        FUNCTION,
        PRE_BLANK_ERROR,
        DONE
    }

    private final Text src;
    private DexError err;
    private DexFunction function;

    public DexRootDeclaration(Text src) {
        this.src = src;
        new Parser();
    }

    public DexFunction function() {
        return function;
    }

    private class Parser {

        private int i;
        private State state = State.PRE_BLANK;
        private int symbolBegin;

        Parser() {
            i = src.begin;
            while (i < src.end) {
                switch (state) {
                    case PRE_BLANK:
                        parsePreBlank();
                        break;
                    case FUNCTION:
                        parseFunction();
                        break;
                    case PRE_BLANK_ERROR:
                        parsePreBlankError();
                        break;
                    case DONE:
                        return;
                }
            }
        }

        void parsePreBlank() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                switch (b) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        continue;
                    case 'f':
                        if (src.bytes[i + 1] == 'u'
                                && src.bytes[i + 2] == 'n'
                                && src.bytes[i + 3] == 'c'
                                && src.bytes[i + 4] == 't'
                                && src.bytes[i + 5] == 'i'
                                && src.bytes[i + 6] == 'o'
                                && src.bytes[i + 7] == 'n') {
                            symbolBegin = i;
                            i = i + 8;
                            switchState(State.FUNCTION);
                            return;
                        }
                    default:
                        reportError();
                        switchState(State.PRE_BLANK_ERROR);
                        return;
                }
            }
        }

        void parseFunction() {
            switch (src.bytes[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    function = new DexFunction(new Text(src.bytes, symbolBegin, src.end));
                    switchState(State.DONE);
                    return;
                default:
                    reportError();
                    switchState(State.PRE_BLANK_ERROR);
            }
        }

        void parsePreBlankError() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                switch (b) {
                    case ' ':
                        switchState(State.PRE_BLANK);
                        return;
                    case '\t':
                        switchState(State.PRE_BLANK);
                        return;
                }
            }
        }

        void switchState(State toState) {
            state = toState;
        }

        void reportError() {
            if (err != null) {
                return;
            }
            err = new DexError(src, i);
        }
    }

    public static List<DexRootDeclaration> parse(Text src) {
        DexRootDeclaration rootDecl = new DexRootDeclaration(src);
        return Arrays.asList(rootDecl);
    }
}
