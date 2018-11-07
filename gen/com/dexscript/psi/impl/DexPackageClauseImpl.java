// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.parser.GoTypes.*;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.dexscript.stubs.GoPackageClauseStub;

public class DexPackageClauseImpl extends GoCompositeElementImpl implements DexPackageClause {

  public DexPackageClauseImpl(ASTNode node) {
    super(node);
  }

  public DexPackageClauseImpl(GoPackageClauseStub stub, IStubElementType stubType) {
    super(stub, stubType);
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

}
