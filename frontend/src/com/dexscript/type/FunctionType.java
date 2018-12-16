package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Function DType: test type compatibility, more permissive than signature
// Function Signature: generic type constraint & where condition
public final class FunctionType implements DType {

    private String description;

    public interface LazyAttachment {
        Object lazyLoad();
    }

    private final TypeSystem ts;

    @NotNull
    private final String name;

    @NotNull
    private final List<DType> params;

    @NotNull
    private final DType ret;

    @NotNull
    private final FunctionSig sig;

    private Object attachment;

    public FunctionType(TypeSystem ts, @NotNull String name, @NotNull List<DType> params, @NotNull DType ret) {
        this(ts, name, params, ret, null);
    }

    public FunctionType(TypeSystem ts, @NotNull String name, @NotNull List<DType> params, @NotNull DType ret, FunctionSig sig) {
        this.ts = ts;
        this.name = name;
        this.params = params;
        this.ret = ret;
        if (sig == null) {
            sig = new FunctionSig(ts, params, ret);
        }
        sig.reparent(this);
        this.sig = sig;
    }

    public void attach(Object attachment) {
        this.attachment = attachment;
    }

    public final Object attachment() {
        if (attachment instanceof LazyAttachment) {
            attachment = ((LazyAttachment) attachment).lazyLoad();
        }
        return attachment;
    }

    @NotNull
    public final String name() {
        return name;
    }

    @NotNull
    public List<DType> params() {
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
    public boolean _isSubType(TypeComparisonContext ctx, DType thatObj) {
        if (!(thatObj instanceof FunctionType)) {
            return false;
        }
        FunctionType that = (FunctionType) thatObj;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.params.size() != that.params.size()) {
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            DType thisParam = this.params.get(i);
            DType thatParam = that.params.get(i);
            if (!thatParam.isAssignableFrom(ctx, thisParam)) {
                if (ctx.shouldLog()) {
                    String reason = String.format("param %s not assignable from %s", thatParam, thisParam);
                    ctx.log(false, this, that, reason);
                }
                return false;
            }
        }
        boolean assignable = this.ret.isAssignableFrom(ctx, that.ret);
        if (ctx.shouldLog()) {
            ctx.log(assignable, this, that, assignable ? "" : String.format("ret %s not assignable from %s", ret, that.ret));
        }
        return assignable;
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
}
