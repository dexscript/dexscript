package com.dexscript.ast.func;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

public class DexAwaitConsumer extends DexAwaitCase {

    private DexIdentifier identifier;
    private DexSig produceSig;
    private DexBlock blk;
    private DexSyntaxError syntaxError;
    private int stmtEnd = -1;

    public DexAwaitConsumer(Text src) {
        super(src);
        new Parser();
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

    public DexAwaitConsumer(String src) {
        this(new Text(src));
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
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i, 'c', 'a', 's', 'e')) {
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
                if (Blank.__(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return null;
        }

        @Expect("identifier")
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
                if (Blank.__(b)) {
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

        @Expect("block")
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
                if (LineEnd.__(b)) {
                    stmtEnd = i;
                    return null;
                }
            }
            stmtEnd = i;
            return null;
        }
    }
}
