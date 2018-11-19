package com.dexscript.parser2;

import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.read.Blank;
import com.dexscript.parser2.read.MatchKeyword;

public class DexFunction {

    private enum State {
        FUNCTION,
        IDENTIFIER,
        LBRACE,
        ERROR,
        DONE
    }

    private final Text src;
    private DexErrorElement err;
    private int functionBegin = -1;
    private DexIdentifier identifier;

    public DexFunction(String src) {
        this(new Text(src));
    }

    public DexFunction(Text src) {
        this.src = src;
        new Parser();
    }

    public boolean valid() {
        return identifier != null;
    }

    public int begin() {
        return functionBegin;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public DexErrorElement err() {
        return err;
    }

    private class Parser {

        private int i;
        private State state = State.FUNCTION;

        Parser() {
            i = src.begin;
            while (i < src.end) {
                switch (state) {
                    case FUNCTION:
                        state = parseFunction();
                        continue;
                    case IDENTIFIER:
                        state = parseIdentifier();
                        continue;
                    case LBRACE:
                        state = parseLBrace();
                        continue;
                    case ERROR:
                        state = skipError();
                        continue;
                }
                reportError();
                return;
            }
        }

        @Expect("function")
        State parseFunction() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == 'f' && MatchKeyword.__(src, i + 1,
                        'u', 'n', 'c', 't', 'i', 'o', 'n')) {
                    functionBegin = i;
                    i = i + 8;
                    return State.IDENTIFIER;
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
            identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.valid()) {
                i = identifier.end();
                return State.LBRACE;
            }
            return State.ERROR;
        }

        @Expect("(")
        State parseLBrace() {
            for (; i < src.end; i++) {
                if (!Blank.__(src.bytes[i])) {
                    break;
                }
            }
            if (src.bytes[i] != '(') {
                return reportError();
            }
            return State.DONE;
        }

        @Expect("blank")
        State skipError() {
            functionBegin = -1;
            identifier = null;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    return State.FUNCTION;
                }
            }
            return State.DONE;
        }

        State reportError() {
            if (state == State.DONE) {
                return State.DONE;
            }
            if (err != null) {
                return State.ERROR;
            }
            err = new DexErrorElement(src, i);
            return State.ERROR;
        }
    }
}
