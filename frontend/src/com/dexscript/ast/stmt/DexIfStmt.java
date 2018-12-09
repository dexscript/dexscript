package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexIfStmt extends DexStatement {

    private DexExpr condition;
    private DexBlock blk;
    private DexElseStmt elseStmt;
    private int ifStmtEnd = -1;

    public DexIfStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexIfStmt(String src) {
        this(new Text(src));
    }

    @Override
    public int end() {
        if (ifStmtEnd == -1) {
            throw new IllegalStateException();
        }
        return ifStmtEnd;
    }

    @Override
    public boolean matched() {
        return ifStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (condition() != null) {
            visitor.visit(condition());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
        if (elseStmt() != null) {
            visitor.visit(elseStmt());
        }
    }

    public DexExpr condition() {
        return condition;
    }

    public DexBlock blk() {
        return blk;
    }

    public DexElseStmt elseStmt() {
        return elseStmt;
    }

    public boolean hasElse() {
        return elseStmt != null && elseStmt.matched();
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::ifKeyword);
        }

        @Expect("if")
        State ifKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'i', 'f')) {
                    i += 2;
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
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::condition;
        }

        @Expect("condition")
        private State condition() {
            condition = DexExpr.parse(src.slice(i));
            condition.reparent(DexIfStmt.this, DexIfStmt.this);
            if (!condition.matched()) {
                return null;
            }
            i = condition.end();
            return this::block;
        }

        @Expect("block")
        private State block() {
            blk = new DexBlock(src.slice(i));
            blk.reparent(DexIfStmt.this, DexIfStmt.this);
            i = blk.end();
            return this::elseSmt;
        }

        @Expect("else")
        private State elseSmt() {
            elseStmt = new DexElseStmt(src.slice(i));
            elseStmt.reparent(DexIfStmt.this, DexIfStmt.this);
            if (elseStmt.matched()) {
                ifStmtEnd = elseStmt.end();
                return null;
            }
            ifStmtEnd = blk.end();
            return null;
        }
    }
}
