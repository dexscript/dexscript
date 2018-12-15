package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Function Type: test type compatibility, more permissive than signature
// Function Signature: generic type constraint & where condition
public final class FunctionType implements Type {

    private String description;

    public interface LazyAttachment {
        Object lazyLoad();
    }

    @NotNull
    private final String name;

    @NotNull
    private final List<Type> params;

    @NotNull
    private final Type ret;

    @NotNull
    private final FunctionSig sig;

    private Object attachment;

    public FunctionType(@NotNull String name, @NotNull List<Type> params, @NotNull Type ret) {
        this(name, params, ret, null);
    }

    public FunctionType(@NotNull String name, @NotNull List<Type> params, @NotNull Type ret, FunctionSig sig) {
        this.name = name;
        this.params = params;
        this.ret = ret;
        if (sig == null) {
            sig = new FunctionSig(params, ret);
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
    public List<Type> params() {
        return params;
    }

    @NotNull
    public Type ret() {
        return ret;
    }

    @NotNull
    public FunctionSig sig() {
        return sig;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type thatObj) {
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
            Type thisParam = this.params.get(i);
            Type thatParam = that.params.get(i);
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
    public String toString() {
        if (description != null) {
            return description;
        }
        description = name + sig.toString();
        return description;
    }
}
