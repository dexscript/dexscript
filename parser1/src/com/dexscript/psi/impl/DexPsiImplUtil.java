package com.dexscript.psi.impl;

import com.dexscript.parser.DexConstants;
import com.dexscript.psi.*;
import com.dexscript.stubs.DexImportSpecStub;
import com.dexscript.stubs.DexPackageClauseStub;
import com.dexscript.stubs.DexParamDeclarationStub;
import com.intellij.lang.ASTNode;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.Conditions.equalTo;

public class DexPsiImplUtil {

    private static final Key<SmartPsiElementPointer<PsiElement>> CONTEXT = Key.create("CONTEXT");

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

    public static String getAlias(@NotNull DexImportSpec importSpec) {
        DexImportSpecStub stub = importSpec.getStub();
        if (stub != null) {
            return stub.getAlias();
        }

        PsiElement identifier = importSpec.getIdentifier();
        if (identifier != null) {
            return identifier.getText();
        }
        return importSpec.isDot() ? "." : null;
    }

    public static boolean isDot(@NotNull DexImportSpec importSpec) {
        DexImportSpecStub stub = importSpec.getStub();
        return stub != null ? stub.isDot() : importSpec.getDot() != null;
    }

    public static String getLocalPackageName(@NotNull String importPath) {
        String fileName = !StringUtil.endsWithChar(importPath, '/') && !StringUtil.endsWithChar(importPath, '\\')
                ? PathUtil.getFileName(importPath)
                : "";
        StringBuilder name = null;
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (!(Character.isLetter(c) || c == '_' || i != 0 && Character.isDigit(c))) {
                if (name == null) {
                    name = new StringBuilder(fileName.length());
                    name.append(fileName, 0, i);
                }
                name.append('_');
            } else if (name != null) {
                name.append(c);
            }
        }
        return name == null ? fileName : name.toString();
    }

    public static boolean shouldDexDeeper(@SuppressWarnings("UnusedParameters") DexImportSpec o) {
        return false;
    }

    public static boolean isCImport(@NotNull DexImportSpec importSpec) {
        return DexConstants.C_PATH.equals(importSpec.getPath());
    }

    @NotNull
    public static String getPath(@NotNull DexImportSpec importSpec) {
        DexImportSpecStub stub = importSpec.getStub();
        return stub != null ? stub.getPath() : importSpec.getImportString().getPath();
    }

    @NotNull
    public static String getPath(@NotNull DexImportString o) {
        return o.getStringLiteral().getDecodedText();
    }


    public static boolean isVariadic(@NotNull DexParamDefinition o) {
        PsiElement parent = o.getParent();
        return parent instanceof DexParameterDeclaration && ((DexParameterDeclaration) parent).isVariadic();
    }

    public static boolean isVariadic(@NotNull DexParameterDeclaration o) {
        DexParamDeclarationStub stub = o.getStub();
        return stub != null ? stub.isVariadic() : o.getTripleDot() != null;
    }

    @NotNull
    public static SyntaxTraverser<PsiElement> dexTraverser() {
        return SyntaxTraverser.psiTraverser().forceDisregardTypes(equalTo(GeneratedParserUtilBase.DUMMY_BLOCK));
    }

    @NotNull
    public static ResolveState createContextOnElement(@NotNull PsiElement element) {
        SmartPointerManager smartPointerManager = SmartPointerManager.getInstance(element.getProject());
        if (smartPointerManager == null) {
            return ResolveState.initial();
        }
        return ResolveState.initial().put(CONTEXT, smartPointerManager.createSmartPsiElementPointer(element));
    }

    @NotNull
    public static DexReference getReference(@NotNull DexReferenceExpression o) {
        return new DexReference(o);
    }

}
