package com.dexscript.psi.impl;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexReferenceExpressionBase;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.OrderedSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DexReference extends DexReferenceBase<DexReferenceExpressionBase> {

    private static final ResolveCache.PolyVariantResolver<DexReference> MY_RESOLVER =
            (r, incompleteCode) -> r.resolveInner();


    public DexReference(DexReferenceExpressionBase iRefExpr) {
        super(iRefExpr, TextRange.from(iRefExpr.getIdentifier().getStartOffsetInParent(), iRefExpr.getIdentifier().getTextLength()));
    }

    private ResolveResult[] resolveInner() {
        if (!myElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        Collection<ResolveResult> result = new OrderedSet<>();
        processResolveVariants(createResolveProcessor(result, myElement));
        return result.toArray(new ResolveResult[result.size()]);
    }

    @Override
    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        if (!myElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        ResolveCache resolveCache = ResolveCache.getInstance(myElement.getProject());
        return resolveCache.resolveWithCaching(this, MY_RESOLVER, false, false);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    public boolean processResolveVariants(@NotNull PsiScopeProcessor processor) {
        PsiFile file = myElement.getContainingFile();
        if (!(file instanceof DexFile)) return false;
        ResolveState state = DexPsiImplUtil.createContextOnElement(myElement);
        DexFile dexFile = (DexFile) file;
        for (DexFunctionDeclaration function : dexFile.getFunctions()) {
            if (!processor.execute(function, state)) {
                return false;
            }
        }
        return true;
    }
}
