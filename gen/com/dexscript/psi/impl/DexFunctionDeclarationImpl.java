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
import com.dexscript.stubs.GoFunctionDeclarationStub;

public class DexFunctionDeclarationImpl extends GoCompositeElementImpl implements DexFunctionDeclaration {

  public DexFunctionDeclarationImpl(ASTNode node) {
    super(node);
  }

  public DexFunctionDeclarationImpl(GoFunctionDeclarationStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitFunctionDeclaration(this);
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
  public DexSignature getSignature() {
    return PsiTreeUtil.getStubChildOfType(this, DexSignature.class);
  }

  @Override
  @NotNull
  public PsiElement getFunction() {
    return notNullChild(findChildByType(FUNCTION));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
