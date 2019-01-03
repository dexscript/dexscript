package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexIfStmt;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.actor.HasAwait;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;

public class TranslateIf implements Translate<DexIfStmt> {

    @Override
    public void handle(OutClass oClass, DexIfStmt iIfStmt) {
        boolean hasAwait = new HasAwait(oClass.typeSystem(), iIfStmt).result();
        if (hasAwait) {
            hasAwait(oClass, iIfStmt);
        } else {
            noAwait(oClass, iIfStmt);
        }
    }

    private void noAwait(OutClass oClass, DexIfStmt iIfStmt) {
        Translate.$(oClass, iIfStmt.condition());
        oClass.g().__("if ((Boolean)"
        ).__(OutValue.of(iIfStmt.condition())
        ).__(") {"
        ).__(new Indent(() -> Translate.$(oClass, iIfStmt.blk()))
        ).__("} ");
        if (iIfStmt.hasElse()) {
            oClass.g().__("else {"
            ).__(new Indent(() -> Translate.$(oClass, iIfStmt.elseStmt()))
            ).__(new Line("} // if"));
        } else {
            oClass.g().__(new Line("// if"));
        }
    }

    private void hasAwait(OutClass oClass, DexIfStmt iIfStmt) {
        Translate.$(oClass, iIfStmt.condition());
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int ifMatchedState = oStateMachine.nextState();
        int elseMatchedState = iIfStmt.hasElse() ? oStateMachine.nextState() : -1;
        int postIfState = oStateMachine.nextState();
        oClass.g().__("if ((Boolean)"
        ).__(OutValue.of(iIfStmt.condition())
        ).__(") {"
        ).__(new Indent(() -> OutStateMethod.call(oClass.g(), ifMatchedState))
        ).__("} ");
        if (elseMatchedState != -1) {
            oClass.g().__("else {"
            ).__(new Indent(() -> OutStateMethod.call(oClass.g(), elseMatchedState))
            ).__(new Line("}"));
        } else {
            oClass.g().__(new Line("// if"));
        }
        // if body
        new OutStateMethod(oClass, ifMatchedState);
        Translate.$(oClass, iIfStmt.blk());
        oClass.g().__("if (!finished()) {"
        ).__(new Indent(() -> OutStateMethod.call(oClass.g(), postIfState))
        ).__("}");
        // else body
        if (elseMatchedState != -1) {
            new OutStateMethod(oClass, elseMatchedState);
            Translate.$(oClass, iIfStmt.elseStmt());
            OutStateMethod.call(oClass.g(), postIfState);
        }
        // post if
        new OutStateMethod(oClass, postIfState);
    }
}
