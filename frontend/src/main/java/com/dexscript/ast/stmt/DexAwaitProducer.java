package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexAwaitProducer extends DexAwaitCase {

    private DexSimpleStatement consumeStmt;
    private DexBlock blk;

    public DexAwaitProducer(Text src) {
        super(src);
        new Parser();
    }

    public static DexAwaitProducer $(String src) {
        return new DexAwaitProducer(new Text(src));
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
        return blk != null && blk.matched();
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

    public DexSimpleStatement consumeStmt() {
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
            consumeStmt = DexSimpleStatement.parse(src.slice(i));
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
