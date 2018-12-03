package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.List;

public class InferFunctionCall implements InferType {

    public interface OnMissingFunction {
        void handle(TypeSystem ts, DexFunctionCallExpr callExpr, String funcName, List<Type> args);
    }

    public static OnMissingFunction ON_MISSING_FUNCTION = (ts, callExpr, funcName, args) -> {
    };

    @Override
    public Type infer(TypeSystem ts, DexExpr elem) {
        DexFunctionCallExpr callExpr = (DexFunctionCallExpr) elem;
        String funcName = callExpr.target().asRef().toString();
        List<Type> args = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            args.add(InferType.inferType(ts, arg));
        }
        List<FunctionType> functions = ts.resolveFunctions(funcName, args);
        if (functions.size() == 0) {
            ON_MISSING_FUNCTION.handle(ts, callExpr, funcName, args);
            return BuiltinTypes.UNDEFINED;
        }
        Type ret = functions.get(0).ret();
        for (int i = 1; i < functions.size(); i++) {
            ret = ret.union(functions.get(i).ret());
        }
        return ret;
    }
}
