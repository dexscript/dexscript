package com.dexscript.parser2;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.expr.DexEndExpr;
import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.expr.DexStringLiteral;
import com.dexscript.parser2.stmt.DexBlock;
import com.dexscript.parser2.stmt.DexReturnStmt;
import com.dexscript.parser2.stmt.DexStatement;

public interface DexTraversal {

    void visited(DexElement elem);

    static void __(DexTraversal traversal, DexFile file) {
        traversal.visited(file.packageClause());
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl instanceof DexFunction) {
                __(traversal, (DexFunction)rootDecl);
            } else {
                throw new UnsupportedOperationException("not implemented");
            }
        }
    }

    static void __(DexTraversal traversal, DexFunction function) {
        traversal.visited(function);
        traversal.visited(function.signature());
        __(traversal, function.block());
    }

    static void __(DexTraversal traversal, DexBlock block) {
        traversal.visited(block);
        for (DexStatement stmt : block.stmts()) {
            __(traversal, stmt);
        }
    }

    static void __(DexTraversal traversal, DexStatement stmt) {
        traversal.visited(stmt);
        if (stmt instanceof DexReturnStmt) {
            __(traversal, (DexReturnStmt)stmt);
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    static void __(DexTraversal traversal, DexReturnStmt stmt) {
        __(traversal, stmt.expr());
    }

    static void __(DexTraversal traversal, DexExpr expr) {
        traversal.visited(expr);
        if (expr instanceof DexStringLiteral) {
            traversal.visited(expr);
        } else if (expr instanceof DexEndExpr) {
            traversal.visited(expr);
        } else {
            throw new UnsupportedOperationException("not implemented: " + expr.getClass());
        }
    }
}
