// copyrightHeader.java
package com.dexscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;

public class DexVisitor extends PsiElementVisitor {

  public void visitBlock(@NotNull DexBlock o) {
    visitGoCompositeElement(o);
  }

  public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration o) {
    visitGoCompositeElement(o);
  }

  public void visitFunctionType(@NotNull DexFunctionType o) {
    visitType(o);
    // visitGoSignatureOwner(o);
  }

  public void visitImportDeclaration(@NotNull DexImportDeclaration o) {
    visitGoCompositeElement(o);
  }

  public void visitImportList(@NotNull DexImportList o) {
    visitGoCompositeElement(o);
  }

  public void visitImportSpec(@NotNull DexImportSpec o) {
    visitGoCompositeElement(o);
  }

  public void visitImportString(@NotNull DexImportString o) {
    visitGoCompositeElement(o);
  }

  public void visitLeftHandExprList(@NotNull DexLeftHandExprList o) {
    visitGoCompositeElement(o);
  }

  public void visitPackageClause(@NotNull DexPackageClause o) {
    visitGoCompositeElement(o);
  }

  public void visitParType(@NotNull DexParType o) {
    visitType(o);
  }

  public void visitParamDefinition(@NotNull DexParamDefinition o) {
    visitGoCompositeElement(o);
  }

  public void visitParameterDeclaration(@NotNull DexParameterDeclaration o) {
    visitGoCompositeElement(o);
  }

  public void visitParameters(@NotNull DexParameters o) {
    visitGoCompositeElement(o);
  }

  public void visitResult(@NotNull DexResult o) {
    visitGoCompositeElement(o);
  }

  public void visitReturnStatement(@NotNull DexReturnStatement o) {
    visitStatement(o);
  }

  public void visitSignature(@NotNull DexSignature o) {
    visitGoCompositeElement(o);
  }

  public void visitStatement(@NotNull DexStatement o) {
    visitGoCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull DexStringLiteral o) {
    visitPsiLanguageInjectionHost(o);
  }

  public void visitType(@NotNull DexType o) {
    visitGoCompositeElement(o);
  }

  public void visitTypeList(@NotNull DexTypeList o) {
    visitType(o);
  }

  public void visitTypeReferenceExpression(@NotNull DexTypeReferenceExpression o) {
    visitGoCompositeElement(o);
  }

  public void visitPsiLanguageInjectionHost(@NotNull PsiLanguageInjectionHost o) {
    visitElement(o);
  }

  public void visitGoCompositeElement(@NotNull GoCompositeElement o) {
    visitElement(o);
  }

}
