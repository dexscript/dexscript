package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.expr.*;

import java.util.*;

public class Invocation {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {
            {
                add(new InferInvocation<DexConsumeExpr>() {
                });
                add(new InferInvocation<DexNewExpr>() {
                });
                add(new InferInvocation<DexEqualExpr>() {
                });
                add(new InferInvocation<DexLessThanExpr>() {
                });
                add(new InferInvocation<DexMethodCallExpr>() {
                });
                add(new InferInvocation<DexFunctionCallExpr>() {
                });
                add(new InferInvocation<DexAddExpr>() {
                });
                add(new InferInvocation<DexIndexExpr>() {
                });
                add(new InferInvocation<DexFieldExpr>() {
                });
            }

            private void add(InferType<?> handler) {
                put((Class<? extends DexExpr>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
            }
        });
    }

    private static class InferInvocation<E extends DexExpr & DexInvocationExpr> implements InferType<E> {

        @Override
        public DType handle(TypeSystem ts, E elem) {
            DexInvocation dexIvc = elem.invocation();
            return Invocation.infer(ts, dexIvc);
        }
    }

    private final String funcName;
    private final List<DType> typeArgs;
    private final List<DType> posArgs;
    private final List<NamedArg> namedArgs;
    private final DType retHint;
    private boolean requireImpl;
    private boolean isGlobalScope;
    private final Set<FunctionType> providedFunctions;

    public Invocation(String funcName, List<DType> typeArgs,
                      List<DType> posArgs, List<NamedArg> namedArgs,
                      DType retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs == null ? Collections.emptyList() : typeArgs;
        this.posArgs = posArgs == null ? Collections.emptyList() : posArgs;
        this.namedArgs = namedArgs == null ? Collections.emptyList() : namedArgs;
        this.retHint = retHint;
        ArrayList<DType> allArgs = new ArrayList<>(this.posArgs);
        for (NamedArg namedArg : this.namedArgs) {
            allArgs.add(namedArg.type());
        }
        providedFunctions = new HashSet<>();
        for (DType allArg : allArgs) {
            if (allArg instanceof CompositeType) {
                providedFunctions.addAll(((CompositeType) allArg).functions());
            }
        }
    }

    public static Invocation ivc(TypeSystem ts, DexInvocation dexIvc) {
        List<DType> posArgs = InferType.inferTypes(ts, dexIvc.posArgs());
        List<DType> typeArgs = ResolveType.resolveTypes(ts, null, dexIvc.typeArgs());
        List<NamedArg> namedArgs = new ArrayList<>();
        DType context = null;
        for (DexNamedArg dexNamedArg : dexIvc.namedArgs()) {
            String argName = dexNamedArg.name().toString();
            DType argType = InferType.$(ts, dexNamedArg.val());
            if (argName.equals("$")) {
                context = argType;
            } else {
                if (context != null) {
                    throw new DexSyntaxException("context argument $ must be the last argument");
                }
                namedArgs.add(new NamedArg(argName, argType));
            }
        }
        Invocation ivc = new Invocation(dexIvc.funcName(), typeArgs, posArgs, namedArgs, null);
        ivc.isGlobalScope(dexIvc.isGlobalScope());
        return ivc;
    }

    public static DType infer(TypeSystem ts, DexInvocation dexIvc) {
        Invocation ivc = ivc(ts, dexIvc);
        Dispatched dispatched = ts.dispatch(ivc);
        return dispatched.ret == null ? ts.UNDEFINED : dispatched.ret;
    }

    // when checking semantic error, the interface function is assumed to have impl
    // when transpile, we set require impl
    public Invocation requireImpl(boolean requireImpl) {
        this.requireImpl = requireImpl;
        return this;
    }

    public boolean requireImpl() {
        return requireImpl;
    }

    public Invocation isGlobalScope(boolean isGlobalScope) {
        this.isGlobalScope = isGlobalScope;
        return this;
    }

    public boolean isGlobalScope() {
        return isGlobalScope;
    }

    public String funcName() {
        return funcName;
    }

    public List<DType> typeArgs() {
        return typeArgs;
    }

    public List<DType> posArgs() {
        return posArgs;
    }

    public List<NamedArg> namedArgs() {
        return namedArgs;
    }

    public DType retHint() {
        return retHint;
    }

    public boolean canProvide(FunctionType func) {
        return providedFunctions.contains(func);
    }

    public int argsCount() {
        // context argument
        return posArgs.size() + namedArgs.size() + 1;
    }

    @Override
    public String toString() {
        StringBuilder desc = new StringBuilder();
        desc.append(funcName);
        if (!typeArgs.isEmpty()) {
            desc.append('<');
            for (int i = 0; i < typeArgs.size(); i++) {
                if (i > 0) {
                    desc.append(", ");
                }
                desc.append(typeArgs.get(i).toString());
            }
            desc.append('>');
        }
        desc.append('(');
        boolean isFirst = true;
        for (DType posArg : posArgs) {
            isFirst = appendMore(desc, isFirst);
            desc.append(posArg.toString());
        }
        for (NamedArg namedArg : namedArgs) {
            isFirst = appendMore(desc, isFirst);
            desc.append(namedArg.name());
            desc.append('=');
            desc.append(namedArg.type().toString());
        }
        desc.append(")");
        if (retHint != null) {
            desc.append(": ");
            desc.append(retHint.toString());
        }
        return desc.toString();
    }

    public boolean appendMore(StringBuilder desc, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
        } else {
            desc.append(", ");
        }
        return isFirst;
    }
}
