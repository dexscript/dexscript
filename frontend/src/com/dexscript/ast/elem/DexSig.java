package com.dexscript.ast.elem;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.*;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexSig extends DexElement {

    private List<DexParam> params;
    private int sigBegin = -1;
    private int sigEnd = -1;
    private DexSyntaxError syntaxError;
    private DexReference ret;

    public DexSig(Text src) {
        super(src);
        new Parser();
    }

    public DexSig(String src) {
        this(new Text(src));
    }

    public List<DexParam> params() {
        return params;
    }

    public DexReference ret() {
        return ret;
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
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
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
                param.reparent(DexSig.this);
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
                ret.reparent(DexSig.this, null);
                sigEnd = ret.end();
            } else {
                reportError();
                sigEnd = i;
            }
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
