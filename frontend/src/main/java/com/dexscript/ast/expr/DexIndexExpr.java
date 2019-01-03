package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.stmt.DexStatement;

import java.util.ArrayList;
import java.util.List;

public class DexIndexExpr extends DexExpr implements DexInvocationExpr {

    private static final int LEFT_RANK = 1;
    private final DexExpr obj;
    private final DexArrayExpr arrayLiteral;
    private DexInvocation invocation;

    public DexIndexExpr(Text src, DexExpr obj) {
        super(src);
        this.obj = obj;
        arrayLiteral = new DexArrayExpr(src);
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int begin() {
        return obj.begin();
    }

    @Override
    public int end() {
        if (!matched()) {
            throw new IllegalStateException();
        }
        return arrayLiteral.end();
    }

    @Override
    public boolean matched() {
        return arrayLiteral.matched();
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(arrayLiteral);
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        if (obj() != null) {
            obj().reparent(this, stmt);
        }
        arrayLiteral.reparent(this, stmt);
    }

    @Override
    public DexInvocation invocation() {
        if (invocation == null) {
            invocation = new DexInvocation(pkg(), "get", obj, new ArrayList<>(), args());
        }
        return invocation;
    }

    public DexExpr obj() { return obj; }

    public List<DexExpr> args() {
        return arrayLiteral.elems();
    }
}
