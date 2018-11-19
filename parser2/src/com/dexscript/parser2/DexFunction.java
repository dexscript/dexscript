package com.dexscript.parser2;

import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.read.A2Z;
import com.dexscript.parser2.read.Blank;
import com.dexscript.parser2.read.DecDigit;
import com.dexscript.parser2.read.MatchKeyword;

public class DexFunction {

    private enum State {
        PRE_BLANK,
        FUNCTION,
        IDENTIFIER,
        LBRACE,
        PRE_BLANK_ERROR,
        DONE
    }

    private final Text src;
    private DexErrorElement err;
    private DexIdentifier identifier;

    public DexFunction(String src) {
        this(new Text(src));
    }

    public DexFunction(Text src) {
        this.src = src;
        new Parser();
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    private class Parser {

        private int i;
        private State state = State.PRE_BLANK;
        private int functionBegin;
        private int identifierBegin;
        private int lbraceBegin;

        Parser() {
            i = src.begin;
            while (i < src.end) {
                switch (state) {
                    case PRE_BLANK:
                        state = parsePreBlank();
                        continue;
                    case FUNCTION:
                        state = parseFunction();
                        continue;
                    case IDENTIFIER:
                        state = parseIdentifier();
                        continue;
                    case PRE_BLANK_ERROR:
                        state = parsePreBlankError();
                        continue;
                    case DONE:
                        return;
                }
            }
        }

        @Expect("blank")
        @Expect("a~z")
        @Expect("A~Z")
        State parseFunction() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (A2Z.__(b)) {
                    identifierBegin = i;
                    i += 1;
                    return State.IDENTIFIER;
                }
                return reportError();
            }
            return State.DONE;
        }

        @Expect("blank")
        @Expect("function")
        State parsePreBlank() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == 'f' && MatchKeyword.__(src, i+1,
                        'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    functionBegin = i;
                    i = i + 8;
                    return State.FUNCTION;
                }
                return reportError();
            }
            return State.DONE;
        }

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State parseIdentifier() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (A2Z.__(b) || DecDigit.__(b) || b == '_') {
                    continue;
                }
                if (Blank.__(b)) {
                    break;
                }
                if (b == '(') {
                    lbraceBegin = i;
                    identifier = new DexIdentifier(new Text(src.bytes, identifierBegin, i));
                    return State.DONE;
                }
                return reportError();
            }
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '(') {
                    lbraceBegin = i;
                    identifier = new DexIdentifier(new Text(src.bytes, identifierBegin, i));
                    return State.DONE;
                }
                return reportError();
            }
            return State.DONE;
        }

        @Expect("blank")
        State parsePreBlankError() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    return State.PRE_BLANK;
                }
            }
            return State.DONE;
        }

        State reportError(String... expectations) {
            if (err != null) {
                return State.PRE_BLANK_ERROR;
            }
            err = new DexErrorElement(src, i, expectations);
            return State.PRE_BLANK_ERROR;
        }
    }
}
