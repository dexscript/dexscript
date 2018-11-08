// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;

public interface DexExpression extends DexTypeOwner {

  @Nullable
  DexType getDexType(@Nullable ResolveState context);

}
