package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexIntegerLiteral;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

public class DexIncrStmt extends DexSimpleStatement {

    private DexValueRef target;
    private int incrStmtEnd = -1;

    public DexIncrStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexIncrStmt(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return target.begin();
    }

    @Override
    public int end() {
        if (incrStmtEnd == -1) {
            throw new IllegalStateException();
        }
        return incrStmtEnd;
    }

    @Override
    public boolean matched() {
        return incrStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (target() != null) {
            visitor.visit(target());
        }
    }

    public DexValueRef target() {
        return target;
    }

    public DexInvocation invocation() {
        return new DexInvocation("Add__", target, new DexIntegerLiteral("1"));
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::target);
        }

        @Expect("value reference")
        private State target() {
            target = new DexValueRef(src);
            target.reparent(DexIncrStmt.this, DexIncrStmt.this);
            if (!target.matched()) {
                return null;
            }
            i = target.end();
            return this::plusPlus;
        }

        @Expect("++")
        private State plusPlus() {
            for (;i<src.end;i++) {
                if (Blank.$(src.bytes[i])) {
                    continue;
                }
                if (Keyword.$(src, i, '+', '+')) {
                    incrStmtEnd = i+2;
                    return null;
                }
                return null;
            }
            return null;
        }
    }
}
