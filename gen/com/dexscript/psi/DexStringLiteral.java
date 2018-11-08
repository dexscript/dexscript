// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.dexscript.psi.impl.DexStringLiteralEscaper;
import com.dexscript.psi.impl.DexStringLiteralImpl;

public interface DexStringLiteral extends DexExpression, PsiLanguageInjectionHost {

  @Nullable
  PsiElement getRawString();

  @Nullable
  PsiElement getString();

  boolean isValidHost();

  @NotNull
  DexStringLiteralImpl updateText(@NotNull String text);

  @NotNull
  DexStringLiteralEscaper createLiteralTextEscaper();

  @NotNull
  String getDecodedText();

}
