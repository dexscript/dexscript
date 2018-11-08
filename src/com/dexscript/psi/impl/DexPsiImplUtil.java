package com.dexscript.psi.impl;

import com.dexscript.psi.*;
import com.dexscript.stubs.DexPackageClauseStub;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DexPsiImplUtil {

    @Nullable
    public static DexType getDexType(@NotNull DexExpression o, @Nullable ResolveState context) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static DexStringLiteralEscaper createLiteralTextEscaper(@NotNull DexStringLiteral o) {
        return new DexStringLiteralEscaper(o);
    }

    @NotNull
    public static String getDecodedText(@NotNull DexStringLiteral o) {
        StringBuilder builder = new StringBuilder();
        TextRange range = ElementManipulators.getManipulator(o).getRangeInElement(o);
        o.createLiteralTextEscaper().decode(range, builder);
        return builder.toString();
    }

    @NotNull
    public static DexStringLiteralImpl updateText(@NotNull DexStringLiteral o, @NotNull String text) {
        if (text.length() > 2) {
            if (o.getString() != null) {
                StringBuilder outChars = new StringBuilder();
                DexStringLiteralEscaper.escapeString(text.substring(1, text.length() - 1), outChars);
                outChars.insert(0, '"');
                outChars.append('"');
                text = outChars.toString();
            }
        }

        ASTNode valueNode = o.getNode().getFirstChildNode();
        assert valueNode instanceof LeafElement;

        ((LeafElement) valueNode).replaceWithText(text);
        return (DexStringLiteralImpl) o;
    }


    public static boolean isValidHost(@NotNull DexStringLiteral o) {
        return PsiTreeUtil.getParentOfType(o, DexImportString.class) == null;
    }

    @Nullable
    public static String getName(@NotNull DexPackageClause packageClause) {
        DexPackageClauseStub stub = packageClause.getStub();
        if (stub != null) return stub.getName();
        PsiElement packageIdentifier = packageClause.getIdentifier();
        return packageIdentifier != null ? packageIdentifier.getText().trim() : null;
    }
}
