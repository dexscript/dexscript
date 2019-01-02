package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

public class DexElseStmt extends DexStatement {

    private DexIfStmt ifStmt;
    private DexBlock blk;
    private DexSyntaxError syntaxError;
    private int elseStmtEnd = -1;

    public DexElseStmt(Text src) {
        super(src);
        new Parser();
    }

    @Override
    public int end() {
        return elseStmtEnd;
    }

    @Override
    public boolean matched() {
        return elseStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (ifStmt() != null && ifStmt().matched()) {
            visitor.visit(ifStmt());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexIfStmt ifStmt() {
        return ifStmt;
    }

    public DexBlock blk() {
        return blk;
    }

    public boolean hasIf() {
        return ifStmt != null && ifStmt.matched();
    }


    private class Parser {
        int i = src.begin;

        Parser() {
            State.Play(this::elseKeyword);
        }

        @Expect("else")
        private State elseKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'e', 'l', 's', 'e')) {
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
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::ifStmtOrBlock;
        }

        @Expect("block")
        @Expect("if")
        State ifStmtOrBlock() {
            ifStmt = new DexIfStmt(src.slice(i));
            ifStmt.reparent(DexElseStmt.this, DexElseStmt.this);
            if (ifStmt.matched()) {
                elseStmtEnd = ifStmt.end();
                return null;
            }
            blk = new DexBlock(src.slice(i));
            blk.reparent(DexElseStmt.this, DexElseStmt.this);
            if (blk.matched()) {
                elseStmtEnd = blk.end();
                return null;
            }
            return this::missingBody;
        }

        State missingBody() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    elseStmtEnd = i;
                    return null;
                }
            }
            elseStmtEnd = i;
            return null;
        }
    }
}
