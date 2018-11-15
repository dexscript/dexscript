// copyrightHeader.java
package com.dexscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;

public class DexVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull DexAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAndExpr(@NotNull DexAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAwaitStatement(@NotNull DexAwaitStatement o) {
    visitStatement(o);
  }

  public void visitBinaryExpr(@NotNull DexBinaryExpr o) {
    visitExpression(o);
  }

  public void visitBlock(@NotNull DexBlock o) {
    visitCompositeElement(o);
  }

  public void visitCallExpr(@NotNull DexCallExpr o) {
    visitExpression(o);
  }

  public void visitCallExprArgs(@NotNull DexCallExprArgs o) {
    visitCompositeElement(o);
  }

  public void visitCastExpr(@NotNull DexCastExpr o) {
    visitExpression(o);
  }

  public void visitConditionalExpr(@NotNull DexConditionalExpr o) {
    visitBinaryExpr(o);
  }

  public void visitExpression(@NotNull DexExpression o) {
    visitTypeOwner(o);
  }

  public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration o) {
    visitNamedSignatureOwner(o);
  }

  public void visitFunctionType(@NotNull DexFunctionType o) {
    visitType(o);
    // visitSignatureOwner(o);
  }

  public void visitImportDeclaration(@NotNull DexImportDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitImportList(@NotNull DexImportList o) {
    visitCompositeElement(o);
  }

  public void visitImportSpec(@NotNull DexImportSpec o) {
    visitNamedElement(o);
  }

  public void visitImportString(@NotNull DexImportString o) {
    visitCompositeElement(o);
  }

  public void visitLeftHandExprList(@NotNull DexLeftHandExprList o) {
    visitCompositeElement(o);
  }

  public void visitLiteral(@NotNull DexLiteral o) {
    visitExpression(o);
  }

  public void visitMulExpr(@NotNull DexMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitNewExpr(@NotNull DexNewExpr o) {
    visitExpression(o);
  }

  public void visitNewExprArgs(@NotNull DexNewExprArgs o) {
    visitCompositeElement(o);
  }

  public void visitOrExpr(@NotNull DexOrExpr o) {
    visitBinaryExpr(o);
  }

  public void visitPackageClause(@NotNull DexPackageClause o) {
    visitCompositeElement(o);
  }

  public void visitParType(@NotNull DexParType o) {
    visitType(o);
  }

  public void visitParamDefinition(@NotNull DexParamDefinition o) {
    visitNamedElement(o);
  }

  public void visitParameterDeclaration(@NotNull DexParameterDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitParameters(@NotNull DexParameters o) {
    visitCompositeElement(o);
  }

  public void visitParenthesesExpr(@NotNull DexParenthesesExpr o) {
    visitExpression(o);
  }

  public void visitReferenceExpression(@NotNull DexReferenceExpression o) {
    visitExpression(o);
  }

  public void visitReplyStatement(@NotNull DexReplyStatement o) {
    visitStatement(o);
  }

  public void visitResult(@NotNull DexResult o) {
    visitCompositeElement(o);
  }

  public void visitReturnStatement(@NotNull DexReturnStatement o) {
    visitStatement(o);
  }

  public void visitServeStatement(@NotNull DexServeStatement o) {
    visitStatement(o);
  }

  public void visitShortVarDeclaration(@NotNull DexShortVarDeclaration o) {
    visitVarSpec(o);
  }

  public void visitSignature(@NotNull DexSignature o) {
    visitCompositeElement(o);
  }

  public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
    visitStatement(o);
  }

  public void visitStatement(@NotNull DexStatement o) {
    visitCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull DexStringLiteral o) {
    visitExpression(o);
    // visitPsiLanguageInjectionHost(o);
  }

  public void visitType(@NotNull DexType o) {
    visitCompositeElement(o);
  }

  public void visitTypeList(@NotNull DexTypeList o) {
    visitType(o);
  }

  public void visitTypeReferenceExpression(@NotNull DexTypeReferenceExpression o) {
    visitCompositeElement(o);
  }

  public void visitUnaryExpr(@NotNull DexUnaryExpr o) {
    visitExpression(o);
  }

  public void visitVarDeclaration(@NotNull DexVarDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitVarDefinition(@NotNull DexVarDefinition o) {
    visitNamedElement(o);
  }

  public void visitVarSpec(@NotNull DexVarSpec o) {
    visitCompositeElement(o);
  }

  public void visitNamedElement(@NotNull DexNamedElement o) {
    visitCompositeElement(o);
  }

  public void visitNamedSignatureOwner(@NotNull DexNamedSignatureOwner o) {
    visitCompositeElement(o);
  }

  public void visitTypeOwner(@NotNull DexTypeOwner o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull DexCompositeElement o) {
    visitElement(o);
  }

}
