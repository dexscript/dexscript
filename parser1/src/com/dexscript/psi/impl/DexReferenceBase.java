package com.dexscript.psi.impl;

import com.dexscript.psi.DexReferenceExpressionBase;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class DexReferenceBase<T extends DexReferenceExpressionBase> extends PsiPolyVariantReferenceBase<T> {

    public static final Key<String> ACTUAL_NAME = Key.create("ACTUAL_NAME");

    public DexReferenceBase(T element, TextRange range) {
        super(element, range);
    }


    @NotNull
    protected PsiScopeProcessor createResolveProcessor(@NotNull Collection<ResolveResult> result,
                                                       @NotNull DexReferenceExpressionBase o) {
        return (element, state) -> {
            if (element.equals(o)) return !result.add(new PsiElementResolveResult(element));
            String name = ObjectUtils.chooseNotNull(state.get(ACTUAL_NAME),
                    element instanceof PsiNamedElement ? ((PsiNamedElement)element).getName() : null);
            if (name != null && o.getIdentifier().textMatches(name)) {
                result.add(new PsiElementResolveResult(element));
                return false;
            }
            return true;
        };
    }

}
