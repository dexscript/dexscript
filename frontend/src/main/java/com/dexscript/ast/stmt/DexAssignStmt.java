package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;

import java.util.ArrayList;
import java.util.List;

public class DexAssignStmt extends DexSimpleStatement {

    private List<DexExpr> targets;
    private DexExpr expr;
    private int assignStmtEnd = -1;

    public DexAssignStmt(Text src) {
        super(src);
        new Parser();
    }

    public static DexAssignStmt $(String src) {
        return new DexAssignStmt(new Text(src));
    }

    @Override
    public int end() {
        if (assignStmtEnd == -1) {
            throw new IllegalStateException();
        }
        return assignStmtEnd;
    }

    @Override
    public boolean matched() {
        return assignStmtEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (targets() != null) {
            for (DexExpr target : targets()) {
                visitor.visit(target);
            }
        }
        if (expr() != null) {
            visitor.visit(expr());
        }
    }

    public List<DexExpr> targets() {
        return targets;
    }

    public DexExpr expr() {
        return expr;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::target);
        }

        @Expect("value reference")
        State target() {
            DexExpr target = DexExpr.parse(src.slice(i));
            target.reparent(DexAssignStmt.this, DexAssignStmt.this);
            if (!target.matched()) {
                return null;
            }
            if (targets == null) {
                targets = new ArrayList<>();
            }
            targets.add(target);
            i = target.end();
            return this::commaOrEqual;
        }

        @Expect(",")
        @Expect("=")
        State commaOrEqual() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::target;
                }
                if (b == '=') {
                    i += 1;
                    return this::expr;
                }
                return null;
            }
            return null;
        }

        @Expect("expression")
        State expr() {
            expr = DexExpr.parse(src.slice(i));
            expr.reparent(DexAssignStmt.this, DexAssignStmt.this);
            if (!expr.matched()) {
                return null;
            }
            assignStmtEnd = expr.end();
            return null;
        }
    }
}
