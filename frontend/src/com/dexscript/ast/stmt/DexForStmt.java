package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexForStmt extends DexStatement {

    private DexStatement initStmt;
    private DexExpr condition;
    private DexStatement postStmt;
    private DexBlock blk;

    public DexForStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexForStmt(String src) {
        this(new Text(src));
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

    }

    public DexBlock blk() {
        return blk;
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
                if (b == '{') {
                    return this::block;
                }
                break;
            }
            if (j == 0) {
                return null;
            }
            return this::prelude;
        }

        @Expect("initStmt;condition;postStmt")
        @Expect("var key in keyValues")
        @Expect("var value of keyValues")
        @Expect("condition")
        @Expect("blank")
        State prelude() {
            int preludeBegin = i;
            int firstSemiColon = -1;
            int secondSemiColon = -1;
            int preludeEnd = -1;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == '{') {
                    preludeEnd = i;
                    break;
                }
                if (b == ';') {
                    if (firstSemiColon == -1) {
                        firstSemiColon = i;
                    } else if (secondSemiColon == -1) {
                        secondSemiColon = i;
                    }
                }
            }
            if (preludeEnd == -1) {
                return null;
            }
            Text prelude = src.slice(preludeBegin, preludeEnd);
            if (firstSemiColon == -1) {
                parse3ClausePrelude(prelude, firstSemiColon, secondSemiColon);
            } else {
                parse1ClausePrelude(prelude);
            }
            return this::block;
        }

        void parse1ClausePrelude(Text prelude) {
            throw new UnsupportedOperationException("not implemented");
        }

        void parse3ClausePrelude(Text prelude, int firstSemiColon, int secondSemiColon) {
            initStmt = parseSimpleStmt(prelude.slice(prelude.begin, firstSemiColon));
            if (secondSemiColon == -1) {
                throw new UnsupportedOperationException("not implemented");
            }
            condition = DexExpr.parse(prelude.slice(firstSemiColon, secondSemiColon));
            postStmt = parseSimpleStmt(prelude.slice(secondSemiColon));
        }

        DexStatement parseSimpleStmt(Text stmtSrc) {
            DexStatement stmt = new DexShortVarDecl(stmtSrc);
            return stmt;
        }

        @Expect("block")
        State block() {
            blk = new DexBlock(src);
            return null;
        }
    }
}
