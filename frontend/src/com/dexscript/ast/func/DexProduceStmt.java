package com.dexscript.ast.func;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

public class DexProduceStmt extends DexStatement {

    private DexExpr produced;
    private DexValueRef target;
    private int produceStmtEnd = -1;
    private DexSyntaxError syntaxError;

    public DexProduceStmt(Text src) {
        super(src);
        this.new Parser();
    }

    public DexProduceStmt(String src) {
        this(new Text(src));
    }

    @Override
    public int end() {
        if (produceStmtEnd == -1) {
            throw new IllegalStateException();
        }
        return produceStmtEnd;
    }

    @Override
    public boolean matched() {
        return produceStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (produced != null) {
            visitor.visit(produced);
        }
        if (target != null) {
            visitor.visit(target);
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public DexValueRef target() {
        return target;
    }

    public DexExpr produced() {
        return produced;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::produce);
        }

        @Expect("produce")
        State produce() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'r', 'e', 's', 'o', 'l', 'v', 'e')) {
                    i += 7;
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
            if (j == 0) {
                return null;
            }
            return this::expr;
        }

        @Expect("expression")
        State expr() {
            produced = DexExpr.parse(src.slice(i));
            if (!produced.matched()) {
                return this::missingExpr;
            }
            i = produced.end();
            return this::rightArrow;
        }

        @Expect("->")
        State rightArrow() {
            for (; i < src.end; i++) {
                if (Blank.$(src.bytes[i])) {
                    continue;
                }
                if (Keyword.$(src, i, '-', '>')) {
                    i += 2;
                    return this::target;
                }
                return this::missingArrow;
            }
            return this::missingArrow;
        }

        State missingArrow() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    produceStmtEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::target;
                }
            }
            produceStmtEnd = i;
            return null;
        }

        @Expect("value reference")
        State target() {
            target = new DexValueRef(src.slice(i));
            if (!target.matched()) {
                reportError();
                return this::missingTarget;
            }
            produceStmtEnd = target.end();
            return null;
        }

        State missingTarget() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    produceStmtEnd = i;
                    return null;
                }
            }
            produceStmtEnd = i;
            return null;
        }

        State missingExpr() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    produceStmtEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::rightArrow;
                }
            }
            produceStmtEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
