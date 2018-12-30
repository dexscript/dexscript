package com.dexscript.type;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// Function DType: test type compatibility, more permissive than signature
// Function Signature: generic type constraint & where condition
public final class FunctionType implements DType {

    private String description;

    private final TypeSystem ts;

    private final DexPackage pkg;

    @NotNull
    private final String name;

    private DType context;

    @NotNull
    private final List<FunctionParam> params;

    @NotNull
    private final DType ret;

    @NotNull
    private final FunctionSig sig;

    private Object impl;

    private FunctionImplProvider implProvider;

    public FunctionType(TypeSystem ts,
                        @NotNull String name,
                        @NotNull List<FunctionParam> params,
                        @NotNull DType ret) {
        this(ts, DexPackage.DUMMY, name, params, ret);
    }

    // non generic function
    public FunctionType(TypeSystem ts,
                        DexPackage pkg,
                        @NotNull String name,
                        @NotNull List<FunctionParam> params,
                        @NotNull DType ret) {
        this.ts = ts;
        this.pkg = pkg;
        this.name = name;
        this.params = params;
        this.ret = ret;
        sig = new FunctionSig(ts, pkg, params, ret);
        sig.reparent(this);
        ts.defineFunction(this);
    }

    // generic function
    public FunctionType(TypeSystem ts,
                        String name,
                        TypeTable localTypeTable,
                        DexSig sig) {
        this(ts, name, localTypeTable, null, sig);
    }

    // method
    public FunctionType(TypeSystem ts,
                        String name,
                        TypeTable localTypeTable,
                        DType self,
                        DexSig sig) {
        this.ts = ts;
        this.pkg = sig.pkg();
        this.name = name;
        this.params = new ArrayList<>();
        if (self != null) {
            this.params.add(new FunctionParam("self", self));
        }
        for (DexParam param : sig.params()) {
            String paramName = param.paramName().toString();
            DType paramType = ResolveType.$(ts, localTypeTable, param.paramType());
            this.params.add(new FunctionParam(paramName, paramType));
        }
        this.ret = ResolveType.$(ts, localTypeTable, sig.ret());
        this.sig = new FunctionSig(ts, sig);
        this.sig.reparent(this);
        ts.defineFunction(this);
    }

    public void implProvider(FunctionImplProvider implProvider) {
        this.implProvider = implProvider;
    }

    public final Object impl() {
        if (implProvider == null) {
            throw new IllegalStateException();
        }
        if (impl == null) {
            impl = implProvider.implOf(this);
        }
        return impl;
    }

    @NotNull
    public final String name() {
        return name;
    }

    @NotNull
    public List<FunctionParam> params() {
        return params;
    }

    @NotNull
    public DType ret() {
        return ret;
    }

    @NotNull
    public FunctionSig sig() {
        return sig;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType thatObj) {
        if (!(thatObj instanceof FunctionType)) {
            return false;
        }
        FunctionType that = (FunctionType) thatObj;
        if (!this.name.equals(that.name)) {
            ctx.addLog("function name not equal", "to", this.name, "from", that.name);
            return false;
        }
        if (this.params.size() != that.params.size()) {
            ctx.addLog("params count not equal", "to", this.params().size(), "from", that.params.size());
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            FunctionParam thisParam = this.params.get(i);
            FunctionParam thatParam = that.params.get(i);
            if (!thisParam.name().equals(thatParam.name())) {
                ctx.addLog("param name not equal",
                        "index", i, "to", thisParam.name(), "from", thatParam.name());
                return false;
            }
            if (!new IsAssignable(ctx, "#" + i + " param", thatParam.type(), thisParam.type()).result()) {
                return false;
            }
        }
        if (!new IsAssignable(ctx, "context", that.context(), this.context()).result()) {
            return false;
        }
        if (!new IsAssignable(ctx, "ret", this.ret, that.ret).result()) {
            return false;
        }
        return true;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        if (description != null) {
            return description;
        }
        description = name + sig.toString();
        return description;
    }

    public FunctionImplProvider implProvider() {
        return implProvider;
    }

    public boolean hasImpl() {
        return implProvider != null;
    }

    public DType context() {
        if (context != null) {
            return context;
        }
        context = ts.context(pkg);
        return context;
    }
}
