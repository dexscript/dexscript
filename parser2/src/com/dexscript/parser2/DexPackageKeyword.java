package com.dexscript.parser2;

import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;

public class DexPackageKeyword {

    private enum State {
        PRE_BLANK,
        PACKAGE,
        PRE_BLANK_ERROR,
        DONE
    }

    private final Text src;
    private DexError err;
    private Text matched;

    public DexPackageKeyword(Text src) {
        this.src = src;
        new Parser();
    }


    public int end() {
        if (matched == null) {
            return src.end;
        }
        return matched.end;
    }

    @Override
    public String toString() {
        if (matched == null) {
            return "<unmatched>";
        }
        return matched.toString();
    }

    private class Parser {

        private int i;
        private State state = State.PRE_BLANK;
        private int packageBegin;

        Parser() {
            i = src.begin;
            while (i < src.end) {
                switch (state) {
                    case PRE_BLANK:
                        parsePreBlank();
                        break;
                    case PACKAGE:
                        parsePackage();
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
                    case 'p':
                        if (src.bytes[i + 1] == 'a'
                                && src.bytes[i + 2] == 'c'
                                && src.bytes[i + 3] == 'k'
                                && src.bytes[i + 4] == 'a'
                                && src.bytes[i + 5] == 'g'
                                && src.bytes[i + 6] == 'e') {
                            packageBegin = i;
                            i = i + 7;
                            switchState(State.PACKAGE);
                            return;
                        }
                    default:
                        reportError();
                        switchState(State.PRE_BLANK_ERROR);
                        return;
                }
            }
        }

        void parsePackage() {
            switch (src.bytes[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    matched = new Text(src.bytes, packageBegin, i);
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
}
