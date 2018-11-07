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
import com.dexscript.stubs.GoParameterDeclarationStub;

public class DexParameterDeclarationImpl extends GoCompositeElementImpl implements DexParameterDeclaration {

  public DexParameterDeclarationImpl(ASTNode node) {
    super(node);
  }

  public DexParameterDeclarationImpl(GoParameterDeclarationStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitParameterDeclaration(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexParamDefinition> getParamDefinitionList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, DexParamDefinition.class);
  }

  @Override
  @NotNull
  public DexType getType() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, DexType.class));
  }

  @Override
  @Nullable
  public PsiElement getTripleDot() {
    return findChildByType(TRIPLE_DOT);
  }

}
