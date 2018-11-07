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
import com.dexscript.stubs.GoImportSpecStub;

public class DexImportSpecImpl extends GoCompositeElementImpl implements DexImportSpec {

  public DexImportSpecImpl(ASTNode node) {
    super(node);
  }

  public DexImportSpecImpl(GoImportSpecStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitImportSpec(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DexImportString getImportString() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, DexImportString.class));
  }

  @Override
  @Nullable
  public PsiElement getDot() {
    return findChildByType(DOT);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

}
