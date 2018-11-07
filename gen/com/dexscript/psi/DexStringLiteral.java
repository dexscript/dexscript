// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;

public interface DexStringLiteral extends PsiLanguageInjectionHost {

  @Nullable
  PsiElement getRawString();

  @Nullable
  PsiElement getString();

  //WARNING: isValidHost(...) is skipped
  //matching isValidHost(DexStringLiteral, ...)
  //methods are not found in null

  //WARNING: updateText(...) is skipped
  //matching updateText(DexStringLiteral, ...)
  //methods are not found in null

  //WARNING: createLiteralTextEscaper(...) is skipped
  //matching createLiteralTextEscaper(DexStringLiteral, ...)
  //methods are not found in null

  //WARNING: getDecodedText(...) is skipped
  //matching getDecodedText(DexStringLiteral, ...)
  //methods are not found in null

}
