package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexForStmt;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.impl.HasAwait;
import com.dexscript.transpile.skeleton.*;

public class TranslateFor implements Translate<DexForStmt> {

    @Override
    public void handle(OutClass oClass, DexForStmt iForStmt) {
        boolean hasAwait = new HasAwait(oClass.typeSystem(), iForStmt).result();
        if (hasAwait) {
            if (iForStmt.isForWith3Clauses()) {
                translateForWith3Clauses(oClass, iForStmt);
            }
        } else {
            if (iForStmt.isForWith3Clauses()) {
                translateForWith3ClausesWithoutAwait(oClass, iForStmt);
            }
        }
    }
    private void translateForWith3ClausesWithoutAwait(OutClass oClass, DexForStmt iForStmt) {
        // init
        if (iForStmt.initStmt() != null) {
            Translate.$(oClass, iForStmt.initStmt());
        }
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int loopState = oStateMachine.nextState();
        int checkConditionState = oStateMachine.nextState();
        int postState = oStateMachine.nextState();
        int afterStmtState = oStateMachine.nextState();
        OutStateMethod.call(oClass.g(), loopState);
        // check condition
        new OutTransientStateMethod(oClass, checkConditionState);
        DexExpr condition = iForStmt.condition();
        Translate.$(oClass, condition);
        // loop
        new OutTransientStateMethod(oClass, loopState);
        OutStateMethod.call(oClass.g(), checkConditionState);
        oClass.g().__("for (;"
        ).__(OutValue.of(condition)
        ).__(";"
        ).__(OutStateMethod.methodName(postState)
        ).__("()) {"
        ).__(new Indent(() -> {
            Translate.$(oClass, iForStmt.blk());
        })).__(new Line("}"));
        OutStateMethod.call(oClass.g(), afterStmtState);
        // post
        new OutTransientStateMethod(oClass, postState);
        if (iForStmt.postStmt() != null) {
            Translate.$(oClass, iForStmt.postStmt());
        }
        OutStateMethod.call(oClass.g(), checkConditionState);
        // after stmt
        new OutStateMethod(oClass, afterStmtState);
    }

    private void translateForWith3Clauses(OutClass oClass, DexForStmt iForStmt) {
        // init
        if (iForStmt.initStmt() != null) {
            Translate.$(oClass, iForStmt.initStmt());
        }
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int checkConditionState = oStateMachine.nextState();
        int loopState = oStateMachine.nextState();
        int postState = oStateMachine.nextState();
        int afterStmtState = oStateMachine.nextState();
        OutStateMethod.call(oClass.g(), checkConditionState);
        // check condition
        new OutStateMethod(oClass, checkConditionState);
        DexExpr condition = iForStmt.condition();
        Translate.$(oClass, condition);
        oClass.g().__("if ("
        ).__(OutValue.of(condition)
        ).__(") {"
        ).__(new Indent(() -> {
            OutStateMethod.call(oClass.g(), loopState);
        })).__("} else {"
        ).__(new Indent(() -> {
            OutStateMethod.call(oClass.g(), afterStmtState);
        })).__(new Line("}"));
        // loop body
        new OutStateMethod(oClass, loopState);
        Translate.$(oClass, iForStmt.blk());
        OutStateMethod.call(oClass.g(), postState);
        // post
        new OutStateMethod(oClass, postState);
        if (iForStmt.postStmt() != null) {
            Translate.$(oClass, iForStmt.postStmt());
        }
        OutStateMethod.call(oClass.g(), checkConditionState);
        // after stmt
        new OutStateMethod(oClass, afterStmtState);
    }
}
