package com.dexscript.parser2;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.expr.DexReference;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexSignature implements DexElement {

    private final Text src;
    private List<DexParam> params;
    private int sigBegin = -1;
    private int sigEnd = -1;
    private DexError err;
    private DexReference ret;

    // for walk up
    private DexElement parent;

    public DexSignature(Text src) {
        this.src = src;
        new Parser();
    }

    public DexSignature(String src) {
        this(new Text(src));
    }

    public List<DexParam> params() {
        return params;
    }

    public DexReference ret() {
        return ret;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        if (sigBegin == -1) {
            throw new IllegalStateException();
        }
        return sigBegin;
    }

    @Override
    public int end() {
        if (sigEnd == -1) {
            throw new IllegalStateException();
        }
        return sigEnd;
    }

    @Override
    public boolean matched() {
        return sigEnd != -1;
    }

    @Override
    public DexError err() {
        return err;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
        if (params() != null) {
            for (DexParam param : params()) {
                param.reparent(this);
            }
        }
        if (ret() != null) {
            ret().reparent(this, null);
        }
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (params() != null) {
            for (DexParam param : params()) {
                visitor.visit(param);
            }
        }
        if (ret() != null) {
            visitor.visit(ret());
        }
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftParen);
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '(') {
                    sigBegin = i;
                    params = new ArrayList<>();
                    i += 1;
                    return this::parameter;
                }
            }
            return null;
        }

        @Expect("parameter")
        State parameter() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                break;
            }
            DexParam param = new DexParam(new Text(src.bytes, i, src.end));
            params.add(param);
            if (param.matched()) {
                i = param.end();
                return this::commaOrRightParen;
            }
            reportError();
            // try to recover from invalid argument
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::parameter;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                if (LineEnd.__(b)) {
                    sigEnd = i;
                    return null;
                }
            }
            sigEnd = i;
            return null;
        }

        @Expect(",")
        @Expect(")")
        State commaOrRightParen() {
            int originalCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::parameter;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
            }
            i = originalCursor;
            return this::missingRightParen;
        }

        State missingRightParen() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.__(b)) {
                    sigEnd = i;
                    return null;
                }
            }
            sigEnd = i;
            return null;
        }

        @Expect(":")
        State colon() {
            int cursorBeforeSearchColon = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::ret;
                }
                sigEnd = cursorBeforeSearchColon;
                return null;
            }
            sigEnd = cursorBeforeSearchColon;
            return null;
        }

        @Expect("type")
        State ret() {
            ret = new DexReference(new Text(src.bytes, i, src.end));
            if (ret.matched()) {
                sigEnd = ret.end();
            } else {
                reportError();
                sigEnd = i;
            }
            return null;
        }

        void reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
        }
    }
}
