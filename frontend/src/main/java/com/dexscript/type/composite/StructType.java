package com.dexscript.type.composite;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexNamedArg;
import com.dexscript.ast.expr.DexStructExpr;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StructType implements CompositeType {

    static {
        InferType.register(DexStructExpr.class, (ts, localTypeTable, elem) -> {
            StructType structType = elem.attachmentOfType(StructType.class);
            if (structType != null) {
                return structType;
            }
            structType = new StructType(ts, elem);
            elem.attach(structType);
            return structType;
        });
    }

    private final TypeSystem ts;
    private final DexStructExpr structExpr;
    private List<FunctionType> functions;

    public StructType(TypeSystem ts, DexStructExpr structExpr) {
        this.ts = ts;
        this.structExpr = structExpr;
        ts.lazyDefineFunctions(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        for (DexNamedArg field : structExpr.fields()) {
            functions.add(getFunc(field));
            functions.add(setFunc(field));
        }
        return functions;
    }

    @NotNull
    private FunctionType getFunc(DexNamedArg field) {
        String fieldName = field.name().toString();
        String funcName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        DType ret = inferType(field.val());
        List<FunctionParam> params = Arrays.asList(new FunctionParam("self", this));
        return new FunctionType(ts, structExpr.pkg(), funcName, params, ret);
    }

    private FunctionType setFunc(DexNamedArg field) {
        String fieldName = field.name().toString();
        String funcName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        List<FunctionParam> params = Arrays.asList(
                new FunctionParam("self", this),
                new FunctionParam("value", inferType(field.val())));
        return new FunctionType(ts, structExpr.pkg(), funcName, params, ts.VOID);
    }

    private DType inferType(DexExpr expr) {
        return ts.widenConst(InferType.$(ts, expr));
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return this.equals(that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
