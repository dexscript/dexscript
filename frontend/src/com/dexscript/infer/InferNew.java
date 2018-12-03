package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexNewExpr;
import com.dexscript.type.*;

import java.util.ArrayList;
import java.util.List;

public class InferNew implements InferType {

    public interface OnMissingFunction {
        void handle(TypeSystem ts, DexNewExpr newExpr, String actorName, List<Type> args);
    }

    public static OnMissingFunction ON_MISSING_FUNCTION = (ts, newExpr, actorName, args) -> {
    };

    @Override
    public Type infer(TypeSystem ts, DexExpr elem) {
        DexNewExpr newExpr = (DexNewExpr) elem;
        ArrayList<Type> args = new ArrayList<>();
        String actorName = newExpr.target().asRef().toString();
        args.add(new StringLiteralType(actorName));
        for (DexExpr arg : newExpr.args()) {
            args.add(InferType.inferType(ts, arg));
        }
        List<FunctionType> functions = ts.resolveFunctions("New__", args);
        if (functions.size() == 0) {
            ON_MISSING_FUNCTION.handle(ts, newExpr, actorName, args);
            return BuiltinTypes.UNDEFINED;
        }
        Type ret = functions.get(0).ret();
        for (int i = 1; i < functions.size(); i++) {
            ret = ret.union(functions.get(i).ret());
        }
        return ret;
    }
}
