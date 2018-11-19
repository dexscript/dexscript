package com.dexscript.parser2;

public class DexIdentifier {

    private enum State {
        PRE_BLANK,
        IDENTIFIER,
        PRE_BLANK_ERROR,
        DONE
    }

    private final Text src;
    private DexErrorElement err;
    private Text matched;

    public DexIdentifier(Text src) {
        this.src = src;
        new Parser();
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
        private int identifierBegin;

        Parser() {
            i = src.begin;
            while (i < src.end) {
                switch (state) {
                    case PRE_BLANK:
                        parsePreBlank();
                        break;
                    case IDENTIFIER:
                        parseIdentifier();
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
                if (b == ' ' || b == '\t' || b == '\r' || b == '\n') {
                    continue;
                }
                if (('a' <= b && b <= 'z') || ('A' <= b && b <= 'Z')) {
                    switchState(State.IDENTIFIER);
                    identifierBegin = i;
                    return;
                }
                reportError("blank", "identifier");
                switchState(State.PRE_BLANK_ERROR);
                return;
            }
        }

        void parseIdentifier() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ' ' || b == '\t' || b == '\r' || b == '\n') {
                    matched = new Text(src.bytes, identifierBegin, i);
                    switchState(State.DONE);
                    return;
                }
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

        void reportError(String... expectations) {
            if (err != null) {
                return;
            }
            err = new DexErrorElement(src, i, expectations);
        }
    }
}
