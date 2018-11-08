// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexImportSpecStub;

public interface DexImportSpec extends DexNamedElement, StubBasedPsiElement<DexImportSpecStub> {

  @NotNull
  DexImportString getImportString();

  @Nullable
  PsiElement getDot();

  @Nullable
  PsiElement getIdentifier();

  String getAlias();

  //WARNING: getLocalPackageName(...) is skipped
  //matching getLocalPackageName(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: isForSideEffects(...) is skipped
  //matching isForSideEffects(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  boolean isDot();

  @NotNull
  String getPath();

  //WARNING: getName(...) is skipped
  //matching getName(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  boolean isCImport();

}
