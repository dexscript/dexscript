package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexIfStmt;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;

public class TranslateIf implements Translate<DexIfStmt> {

    @Override
    public void handle(OutClass oClass, DexIfStmt iIfStmt) {
        Translate.$(oClass, iIfStmt.condition());
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int ifMatchedState = oStateMachine.nextState();
        int elseMatchedState = iIfStmt.hasElse() ? oStateMachine.nextState() : -1;
        int postIfState = oStateMachine.nextState();
        oClass.g().__("if ((Boolean)"
        ).__(OutValue.of(iIfStmt.condition())
        ).__(") {"
        ).__(new Indent(() -> OutStateMethod.call(oClass.g(), ifMatchedState))
        ).__(new Line("}"));
        if (elseMatchedState != -1) {
            oClass.g().__("else {"
            ).__(new Indent(() -> OutStateMethod.call(oClass.g(), elseMatchedState))
            ).__(new Line("}"));
        }
        new OutStateMethod(oClass, ifMatchedState);
        Translate.$(oClass, iIfStmt.blk());
        OutStateMethod.call(oClass.g(), postIfState);
        if (elseMatchedState != -1) {
            new OutStateMethod(oClass, elseMatchedState);
            Translate.$(oClass, iIfStmt.elseStmt());
            OutStateMethod.call(oClass.g(), postIfState);
        }
        new OutStateMethod(oClass, postIfState);
    }
}
