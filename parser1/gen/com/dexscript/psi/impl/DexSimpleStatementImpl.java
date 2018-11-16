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

public class DexSimpleStatementImpl extends DexStatementImpl implements DexSimpleStatement {

  public DexSimpleStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitSimpleStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DexAssignmentStatement getAssignmentStatement() {
    return PsiTreeUtil.getChildOfType(this, DexAssignmentStatement.class);
  }

  @Override
  @Nullable
  public DexLeftHandExprList getLeftHandExprList() {
    return PsiTreeUtil.getChildOfType(this, DexLeftHandExprList.class);
  }

  @Override
  @Nullable
  public DexShortVarDeclaration getShortVarDeclaration() {
    return PsiTreeUtil.getChildOfType(this, DexShortVarDeclaration.class);
  }

}
