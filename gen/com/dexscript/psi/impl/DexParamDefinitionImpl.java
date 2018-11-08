// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexParamDefinitionStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexParamDefinitionImpl extends DexNamedElementImpl<DexParamDefinitionStub> implements DexParamDefinition {

  public DexParamDefinitionImpl(@NotNull DexParamDefinitionStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexParamDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitParamDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
