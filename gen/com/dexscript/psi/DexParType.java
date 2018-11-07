// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexParType extends DexType {

  @NotNull
  DexType getType();

  @NotNull
  PsiElement getLparen();

  @NotNull
  PsiElement getRparen();

  //WARNING: getActualType(...) is skipped
  //matching getActualType(DexParType, ...)
  //methods are not found in null

}
