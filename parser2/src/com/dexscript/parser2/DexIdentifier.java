package com.dexscript.parser2;

import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.read.A2Z;
import com.dexscript.parser2.read.Blank;

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

    public DexIdentifier(String src) {
        this(new Text(src));
    }

    public DexIdentifier(Text src) {
        this.src = src;
        new Parser();
    }

    public boolean valid() {
        return matched != null;
    }

    public int end() {
        return matched.end;
    }

    @Override
    public String toString() {
        if (matched == null) {
            return "<unmatched>" + src + "</unmatched>";
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
                        state = parsePreBlank();
                        continue;
                    case IDENTIFIER:
                        state = parseIdentifier();
                        continue;
                }
                return;
            }
        }

        @Expect("blank")
        @Expect("a~z")
        @Expect("A~Z")
        State parsePreBlank() {
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

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State parseIdentifier() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b) || b == '(') {
                    matched = new Text(src.bytes, identifierBegin, i);
                    return State.DONE;
                }
            }
            matched = new Text(src.bytes, identifierBegin, src.end);
            return State.DONE;
        }

        State reportError() {
            if (err != null) {
                return State.PRE_BLANK_ERROR;
            }
            err = new DexErrorElement(src, i);
            return State.PRE_BLANK_ERROR;
        }
    }
}
