package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexAwaitProducer extends DexAwaitCase {

    private DexStatement consumeStmt;
    private DexBlock blk;

    public DexAwaitProducer(Text src) {
        super(src);
        new Parser();
    }

    public DexAwaitProducer(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return blk.end();
    }

    @Override
    public boolean matched() {
        return blk != null;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (consumeStmt() != null) {
            visitor.visit(consumeStmt());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
    }

    public DexStatement consumeStmt() {
        return consumeStmt;
    }

    public DexBlock blk() {
        return blk;
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
                return this::consumeStmt;
            }
            return null;
        }

        @Expect("consumeStmt statement")
        State consumeStmt() {
            boolean foundConsumeSymbol = false;
            int consumeStmtBegin = i;
            int consumeStmtEnd = -1;
            for (; i < src.end; i++) {
                if (Keyword.$(src, i, '<', '-')) {
                    foundConsumeSymbol = true;
                    continue;
                }
                byte b = src.bytes[i];
                if (foundConsumeSymbol && b == '{') {
                    consumeStmtEnd = i;
                    break;
                }
            }
            if (consumeStmtEnd == -1) {
                return null;
            }
            consumeStmt = DexStatement.parse(src.slice(consumeStmtBegin, consumeStmtEnd));
            consumeStmt.reparent(DexAwaitProducer.this, null);
            if (!consumeStmt.matched()) {
                return null;
            }
            i = consumeStmt.end();
            return this::block;
        }

        @Expect("blk")
        private State block() {
            blk = new DexBlock(src.slice(i));
            blk.reparent(DexAwaitProducer.this, null);
            return null;
        }
    }
}
