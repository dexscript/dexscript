package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexAssignStmt extends DexSimpleStatement {

    private DexSyntaxError syntaxError;
    private List<DexValueRef> targets;
    private DexExpr expr;
    private int assignStmtEnd = -1;

    public DexAssignStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexAssignStmt(String src) {
        this(new Text(src));
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
            for (DexValueRef target : targets()) {
                visitor.visit(target);
            }
        }
        if (expr() != null) {
            visitor.visit(expr());
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public List<DexValueRef> targets() {
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
            DexValueRef target = new DexValueRef(src.slice(i));
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
                return this::missingExpr;
            }
            return endWith(expr.end());
        }

        State endWith(int end) {
            assignStmtEnd = end;
            return null;
        }

        State missingExpr() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    assignStmtEnd = i;
                    return null;
                }
            }
            assignStmtEnd = i;
            return null;
        }
    }
}
