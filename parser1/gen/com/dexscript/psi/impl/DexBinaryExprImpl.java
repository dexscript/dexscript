// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.psi.*;

public class DexBinaryExprImpl extends DexExpressionImpl implements DexBinaryExpr {

  public DexBinaryExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitBinaryExpr(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DexExpression.class);
  }

  @Override
  @NotNull
  public DexExpression getLeft() {
    List<DexExpression> p1 = getExpressionList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public DexExpression getRight() {
    List<DexExpression> p1 = getExpressionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
