/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexscript.psi;

import com.dexscript.parser.DexLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class DexTokenType extends IElementType {

  public static final IElementType LINE_COMMENT = new DexTokenType("DEX_LINE_COMMENT");
  public static final IElementType MULTILINE_COMMENT = new DexTokenType("DEX_MULTILINE_COMMENT");
  public static final IElementType WS = new DexTokenType("DEX_WHITESPACE");
  public static final IElementType NLS = new DexTokenType("DEX_WS_NEW_LINES");

  public DexTokenType(@NotNull String debug) {
    super(debug, DexLanguage.INSTANCE);
  }
}
