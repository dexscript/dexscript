package com.dexscript.ast.stmt;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.core.*;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;
import com.dexscript.ast.type.DexType;

import java.util.List;

public class DexAwaitConsumer extends DexAwaitCase {

    private DexIdentifier identifier;
    private DexSig produceSig;
    private DexBlock blk;
    private DexSyntaxError syntaxError;
    private DexSig callFuncSig;
    private int stmtEnd = -1;

    public DexAwaitConsumer(Text src) {
        super(src);
        new Parser();
    }

    public static DexAwaitConsumer $(String src) {
        return new DexAwaitConsumer(new Text(src));
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return stmtEnd;
    }

    @Override
    public boolean matched() {
        return stmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (produceSig() != null) {
            visitor.visit(produceSig());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexSig produceSig() {
        return produceSig;
    }

    public DexBlock blk() {
        return blk;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public List<DexParam> params() {
        return produceSig.params();
    }

    public DexType ret() {
        return produceSig.ret();
    }

    public List<DexStatement> stmts() {
        return blk().stmts();
    }

    public DexActor actor() {
        DexElement current = parent;
        while (current != null) {
            if (current instanceof DexActor) {
                return (DexActor) current;
            }
            current = current.parent();
        }
        throw new DexSyntaxException("actor not found");
    }

    public DexSig callFuncSig() {
        if (callFuncSig != null) {
            return callFuncSig;
        }
        StringBuilder sig = new StringBuilder("(");
        boolean isFirst = true;
        for (DexTypeParam typeParam : produceSig.typeParams()) {
            isFirst = appendMore(sig, isFirst);
            sig.append(typeParam.toString());
        }
        appendMore(sig, isFirst);
        sig.append("self: ");
        sig.append(actor().actorName());
        for (DexParam param : produceSig.params()) {
            sig.append(", ");
            sig.append(param.toString());
        }
        sig.append("): ");
        sig.append(ret().toString());
        callFuncSig = new DexSig(new Text(sig.toString()));
        callFuncSig.reparent(this);
        return callFuncSig;
    }

    private static boolean appendMore(StringBuilder sig, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
        } else {
            sig.append(", ");
        }
        return isFirst;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::caseKeyword);
        }

        @Expect("case")
        State caseKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'c', 'a', 's', 'e')) {
                    i += 4;
                    return this::blank;
                }
                return null;
            }
            return null;
        }

        @Expect("blank")
        State blank() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                if (Blank.$(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return null;
        }

        @Expect("paramName")
        State identifier() {
            identifier = new DexIdentifier(new Text(src.bytes, i, src.end));
            if (identifier.matched()) {
                i = identifier.end();
                return this::leftParen;
            }
            return null;
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '(') {
                    return this::signature;
                }
                return null;
            }
            return null;
        }

        @Expect("signature")
        State signature() {
            produceSig = new DexSig(src.slice(i));
            produceSig.reparent(DexAwaitConsumer.this);
            if (produceSig.matched()) {
                i = produceSig.end();
                return this::block;
            }
            return this::reportError;
        }

        @Expect("blk")
        State block() {
            blk = new DexBlock(src.slice(i));
            blk.reparent(DexAwaitConsumer.this, null);
            if (blk.matched()) {
                stmtEnd = blk.end();
                return null;
            }
            return this::reportError;
        }

        State reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    stmtEnd = i;
                    return null;
                }
            }
            stmtEnd = i;
            return null;
        }
    }
}
