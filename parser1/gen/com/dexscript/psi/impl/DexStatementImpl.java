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

public class DexStatementImpl extends DexCompositeElementImpl implements DexStatement {

  public DexStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DexBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, DexBlock.class);
  }

  @Override
  @Nullable
  public DexVarDeclaration getVarDeclaration() {
    return PsiTreeUtil.getChildOfType(this, DexVarDeclaration.class);
  }

}
