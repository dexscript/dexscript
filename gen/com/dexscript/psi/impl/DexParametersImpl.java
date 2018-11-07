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
import com.dexscript.stubs.GoParametersStub;

public class DexParametersImpl extends GoCompositeElementImpl implements DexParameters {

  public DexParametersImpl(ASTNode node) {
    super(node);
  }

  public DexParametersImpl(GoParametersStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitParameters(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexParameterDeclaration> getParameterDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, DexParameterDeclaration.class);
  }

  @Override
  @Nullable
  public DexType getType() {
    return PsiTreeUtil.getStubChildOfType(this, DexType.class);
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
