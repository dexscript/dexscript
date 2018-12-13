package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.ast.inf.DexInfTypeParam;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InterfaceType implements NamedType, GenericType, FunctionsProvider {

    private final TypeTable typeTable;
    private final FunctionTable functionTable;
    private final DexInterface inf;
    private List<Type> typeArgs;
    private List<FunctionType> members;
    private List<Type> typeParams;

    public InterfaceType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable, @NotNull DexInterface inf) {
        this(typeTable, functionTable, inf, null);
    }

    public InterfaceType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable,
                         @NotNull DexInterface inf, List<Type> typeArgs) {
        this.typeArgs = typeArgs;
        this.typeTable = typeTable;
        this.functionTable = functionTable;
        this.inf = inf;
        if (typeArgs == null) {
            typeTable.define(this);
        }
        functionTable.lazyDefine(this);
    }

    @Override
    public @NotNull String name() {
        return inf.identifier().toString();
    }

    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        List<Type> typeArgs = this.typeArgs;
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        members = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(typeTable);
        for (int i = 0; i < inf.typeParams().size(); i++) {
            DexInfTypeParam typeParam = inf.typeParams().get(i);
            String typeParamName = typeParam.paramName().toString();
            localTypeTable.define(typeParamName, typeArgs.get(i));
        }
        for (DexInfMethod method : inf.methods()) {
            addInfMethod(localTypeTable, method);
        }
        for (DexInfFunction function : inf.functions()) {
            addInfFunction(localTypeTable, function);
        }
        return members;
    }

    private void addInfFunction(TypeTable localTypeTable, DexInfFunction infFunction) {
        String name = infFunction.identifier().toString();
        List<Type> params = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        Type ret = ResolveType.$(localTypeTable, infFunction.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    private void addInfMethod(TypeTable localTypeTable, DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        Type ret = ResolveType.$(localTypeTable, infMethod.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        if (ctx.shouldLog()) {
            ctx.log(">>> check " + this + " is assignable from " + that);
        }
        ctx.putSubstituted(that, this);
        TypeComparisonContext subCtx = new TypeComparisonContext(ctx);
        for (FunctionType member : functions()) {
            subCtx.undefine(member);
        }
        for (FunctionType member : functions()) {
            if (!functionTable.isDefined(subCtx, member)) {
                if (ctx.shouldLog()) {
                    ctx.log("<<< " + this + " is not assignable from " + that + " because missing " + member);
                }
                return false;
            }
        }
        subCtx.commit();
        if (ctx.shouldLog()) {
            ctx.log("<<< " + this + " is assignable from " + that);
        }
        return true;
    }

    @Override
    public String description() {
        if (typeArgs != null && typeArgs.size() > 0) {
            StringBuilder desc = new StringBuilder(name());
            desc.append('<');
            for (int i = 0; i < typeArgs.size(); i++) {
                if (i > 0) {
                    desc.append(", ");
                }
                Type typeArg = typeArgs.get(i);
                desc.append(typeArg.toString());
            }
            desc.append('>');
            return desc.toString();
        }
        return name();
    }

    @Override
    public String toString() {
        if (typeArgs == null) {
            return name();
        }
        return name() + "@" + System.identityHashCode(this);
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new InterfaceType(typeTable, functionTable, inf, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexInfTypeParam typeParam : inf.typeParams()) {
                typeParams.add(ResolveType.$(typeTable, typeParam.paramType()));
            }
        }
        return typeParams;
    }

    public List<Type> typeArgs() {
        return typeArgs;
    }
}
