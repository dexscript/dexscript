// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexPackageClauseStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexPackageClauseImpl extends DexStubbedElementImpl<DexPackageClauseStub> implements DexPackageClause {

  public DexPackageClauseImpl(@NotNull DexPackageClauseStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexPackageClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitPackageClause(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getPackage() {
    return notNullChild(findChildByType(PACKAGE));
  }

  @Nullable
  public String getName() {
    return DexPsiImplUtil.getName(this);
  }

}
