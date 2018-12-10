package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexForStmt;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.impl.HasAwait;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;
import com.dexscript.transpile.skeleton.OutTransientStateMethod;

public class TranslateForWith3Clauses implements Translate<DexForStmt> {

    @Override
    public void handle(OutClass oClass, DexForStmt iForStmt) {
        boolean hasAwait = new HasAwait(oClass.typeSystem(), iForStmt).result();
        if (hasAwait) {
            hasAwait(oClass, iForStmt);
        } else {
            noAwait(oClass, iForStmt);
        }
    }

    private void noAwait(OutClass oClass, DexForStmt iForStmt) {
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

    private void hasAwait(OutClass oClass, DexForStmt iForStmt) {
        // init
        if (iForStmt.initStmt() != null) {
            Translate.$(oClass, iForStmt.initStmt());
        }
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int checkConditionState = oStateMachine.nextState();
        int loopState = oStateMachine.nextState();
        int trampolineState = oStateMachine.nextState();
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
            OutStateMethod.call(oClass.g(), trampolineState);
        })).__("} else {"
        ).__(new Indent(() -> {
            OutStateMethod.call(oClass.g(), afterStmtState);
        })).__(new Line("}"));
        // loop body
        new OutStateMethod(oClass, loopState);
        Translate.$(oClass, iForStmt.blk());
        OutStateMethod.call(oClass.g(), postState);
        // trampoline
        new OutStateMethod(oClass, trampolineState);
        oStateMachine.yieldTo(oClass.g(), loopState);
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
