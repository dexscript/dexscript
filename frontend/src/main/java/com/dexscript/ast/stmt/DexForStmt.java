package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexForStmt extends DexStatement {

    private DexExpr condition;
    private DexSimpleStatement initStmt;
    private DexSimpleStatement postStmt;
    private DexBlock blk;

    public DexForStmt(Text src) {
        super(src);
        new Parser();
    }

    public static DexForStmt $(String src) {
        return new DexForStmt(new Text(src));
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
        if (initStmt() != null) {
            visitor.visit(initStmt());
        }
        if (condition() != null) {
            visitor.visit(condition());
        }
        if (postStmt() != null) {
            visitor.visit(postStmt());
        }
        if (blk() != null) {
            visitor.visit(blk());
        }
    }

    public DexBlock blk() {
        return blk;
    }

    public boolean isForCondition() {
        if (!matched()) {
            return false;
        }
        return initStmt == null && condition != null;
    }

    public boolean isForever() {
        if (!matched()) {
            return false;
        }
        return initStmt == null && condition == null;
    }

    public boolean isForWith3Clauses() {
        if (!matched()) {
            return false;
        }
        return initStmt != null || postStmt != null;
    }

    public DexExpr condition() {
        return condition;
    }

    public DexSimpleStatement initStmt() {
        return initStmt;
    }

    public DexSimpleStatement postStmt() {
        return postStmt;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::forKeyword);
        }

        @Expect("for")
        State forKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'f', 'o', 'r')) {
                    i += 3;
                    return this::blankOrLeftBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("blank")
        @Expect("{")
        State blankOrLeftBrace() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '{') {
                    return this::block;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::firstClause;
        }

        @Expect("condition")
        @Expect("init statement")
        @Expect("for in")
        @Expect("for of")
        State firstClause() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ';') {
                    i += 1;
                    return this::secondClause;
                }
                break;
            }
            initStmt = DexSimpleStatement.parse(src.slice(i));
            initStmt.reparent(DexForStmt.this, null);
            if (initStmt.matched()) {
                i = initStmt.end();
                return this::firstSemiColon;
            }
            return null;
        }

        @Expect(";")
        State firstSemiColon() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ';') {
                    i += 1;
                    return this::secondClause;
                }
                if (b == '{' && initStmt instanceof DexExprStmt) {
                    condition = ((DexExprStmt) initStmt).expr();
                    condition.reparent(DexForStmt.this, null);
                    initStmt = null;
                    return this::block;
                }
                return null;
            }
            return null;
        }

        @Expect("condition")
        State secondClause() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ';') {
                    i += 1;
                    return this::thirdClause;
                }
                break;
            }
            condition = DexExpr.parse(src.slice(i));
            condition.reparent(DexForStmt.this, null);
            if (!condition.matched()) {
                return null;
            }
            i = condition.end();
            return this::secondSemiColon;
        }

        @Expect(";")
        State secondSemiColon() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ';') {
                    i += 1;
                    return this::thirdClause;
                }
                return null;
            }
            return null;
        }

        @Expect("post statement")
        State thirdClause() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '{') {
                    return this::block;
                }
                break;
            }
            postStmt = DexSimpleStatement.parse(src.slice(i));
            postStmt.reparent(DexForStmt.this, null);
            if (!postStmt.matched()) {
                return null;
            }
            i = postStmt.end();
            return this::block;
        }

        @Expect("block")
        State block() {
            blk = new DexBlock(src.slice(i));
            blk.reparent(DexForStmt.this, null);
            return null;
        }
    }
}
