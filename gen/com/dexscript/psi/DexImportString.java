// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexImportString extends GoCompositeElement {

  @NotNull
  DexStringLiteral getStringLiteral();

  //WARNING: getReferences(...) is skipped
  //matching getReferences(DexImportString, ...)
  //methods are not found in null

  //WARNING: resolve(...) is skipped
  //matching resolve(DexImportString, ...)
  //methods are not found in null

  //WARNING: getPath(...) is skipped
  //matching getPath(DexImportString, ...)
  //methods are not found in null

  //WARNING: getPathTextRange(...) is skipped
  //matching getPathTextRange(DexImportString, ...)
  //methods are not found in null

}
