// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexBlock extends GoCompositeElement {

  @NotNull
  List<DexStatement> getStatementList();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(DexBlock, ...)
  //methods are not found in null

}
