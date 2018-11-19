// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexVarSpecStub;

public interface DexVarSpec extends DexCompositeElement, StubBasedPsiElement<DexVarSpecStub> {

  @NotNull
  List<DexExpression> getExpressionList();

  @Nullable
  DexType getType();

  @NotNull
  List<DexVarDefinition> getVarDefinitionList();

  @Nullable
  PsiElement getAssign();

  @Nullable
  PsiElement getColon();

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(DexVarSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(DexVarSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getRightExpressionsList(...) is skipped
  //matching getRightExpressionsList(DexVarSpec, ...)
  //methods are not found in DexPsiImplUtil

}
