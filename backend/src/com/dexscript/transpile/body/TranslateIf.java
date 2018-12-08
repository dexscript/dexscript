package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexIfStmt;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;

public class TranslateIf implements Translate<DexIfStmt> {
    @Override
    public void handle(OutClass oClass, DexIfStmt iIfStmt) {
        Translate.$(oClass, iIfStmt.condition());
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int ifMatchedState = oStateMachine.nextState();
        int postIfState = oStateMachine.nextState();
        oClass.g().__("if ("
        ).__(OutValue.of(iIfStmt.condition())
        ).__(") {"
        ).__(new Indent(() -> {
            oClass.g().__(OutStateMethod.methodName(ifMatchedState)).__(new Line("();"));
        })).__(new Line("}"));
        new OutStateMethod(oClass, ifMatchedState);
        Translate.$(oClass, iIfStmt.blk());
        oClass.g().__(OutStateMethod.methodName(postIfState)).__(new Line("();"));
        new OutStateMethod(oClass, postIfState);
    }
}
