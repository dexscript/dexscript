package com.dexscript.ast.elem;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.*;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;
import com.dexscript.ast.type.DexVoidType;

import java.util.ArrayList;
import java.util.List;

public class DexSig extends DexElement {

    private List<DexTypeParam> typeParams;
    private List<DexParam> params;
    private DexSyntaxError syntaxError;
    private DexType ret;
    private int sigEnd = -1;

    public DexSig(Text src) {
        super(src);
        new Parser();
    }

    public static DexSig $(String src) {
        DexSig elem = new DexSig(new Text(src));
        elem.attach(DexPackage.DUMMY);
        return elem;
    }

    public List<DexTypeParam> typeParams() {
        return typeParams;
    }

    public List<DexParam> params() {
        return params;
    }

    public DexType ret() {
        return ret;
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
        if (typeParams() != null) {
            for (DexTypeParam typeParam : typeParams()) {
                visitor.visit(typeParam);
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
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    typeParams = new ArrayList<>();
                    params = new ArrayList<>();
                    i += 1;
                    return this::leftAngleBracketOrParameter;
                }
            }
            return null;
        }

        @Expect("<")
        @Expect("parameter")
        State leftAngleBracketOrParameter() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '<') {
                    return this::typeParam;
                }
                break;
            }
            return this::paramOrRightParen;
        }

        @Expect("paramType parameter")
        State typeParam() {
            DexTypeParam typeParam = new DexTypeParam(src.slice(i));
            typeParam.reparent(DexSig.this);
            typeParams.add(typeParam);
            i = typeParam.end();
            return this::moreTypeParam;
        }

        @Expect(",")
        @Expect(")")
        State moreTypeParam() {
            int origCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::leftAngleBracketOrParameter;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                i = origCursor;
                return this::missingRightParen;
            }
            i = origCursor;
            return this::missingRightParen;
        }

        @Expect("parameter")
        @Expect(")")
        State paramOrRightParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                break;
            }
            DexParam param = new DexParam(src.slice(i));
            param.reparent(DexSig.this);
            params.add(param);
            if (!param.matched()) {
                return this::missingParam;
            }
            i = param.end();
            return this::moreParam;
        }

        @Expect(",")
        @Expect(")")
        State moreParam() {
            int origCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::paramOrRightParen;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                i = origCursor;
                return this::missingRightParen;
            }
            i = origCursor;
            return this::missingRightParen;
        }

        @Expect(":")
        State colon() {
            int cursorBeforeSearchColon = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::ret;
                }
                ret = new DexVoidType(src.slice(i), true);
                ret.reparent(DexSig.this);
                sigEnd = cursorBeforeSearchColon;
                return null;
            }
            ret = new DexVoidType(src.slice(i), true);
            ret.reparent(DexSig.this);
            sigEnd = cursorBeforeSearchColon;
            return null;
        }

        @Expect("type")
        State ret() {
            ret = DexType.parse(src.slice(i));
            if (ret.matched()) {
                ret.reparent(DexSig.this);
                sigEnd = ret.end();
            } else {
                reportError();
                sigEnd = i;
            }
            return null;
        }

        State missingParam() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::paramOrRightParen;
                }
                if (b == ')') {
                    i += 1;
                    return this::colon;
                }
                if (LineEnd.$(b)) {
                    sigEnd = i;
                    return null;
                }
            }
            sigEnd = i;
            return null;
        }

        State missingRightParen() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    sigEnd = i;
                    return null;
                }
            }
            sigEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
