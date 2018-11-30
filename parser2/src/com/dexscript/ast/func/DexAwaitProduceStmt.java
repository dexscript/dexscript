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

public class DexAwaitProduceStmt extends DexAwaitCase {

    private DexIdentifier identifier;
    private DexSig sig;
    private DexBlock blk;
    private DexSyntaxError syntaxError;
    private int produceStmtEnd = -1;

    public DexAwaitProduceStmt(Text src) {
        super(src);
        new Parser();
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return produceStmtEnd;
    }

    @Override
    public boolean matched() {
        return produceStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (sig() != null) {

        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexSig sig() {
        return sig;
    }

    public DexBlock blk() {
        return blk;
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public DexAwaitProduceStmt(String src) {
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
            sig = new DexSig(src.slice(i));
            if (sig.matched()) {
                i = sig.end();
                return this::block;
            }
            return this::reportError;
        }

        @Expect("blk")
        State block() {
            blk = new DexBlock(src.slice(i));
            if (blk.matched()) {
                produceStmtEnd = blk.end();
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
                    produceStmtEnd = i;
                    return null;
                }
            }
            produceStmtEnd = i;
            return null;
        }
    }
}
