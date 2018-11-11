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

package com.dexscript.parser;

import com.dexscript.lexer.DexLexer;
import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexFileElementType;
import com.dexscript.psi.DexTokenType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.dexscript.psi.DexTypes.*;

public class DexParserDefinition implements ParserDefinition {


  public static final TokenSet WHITESPACES = TokenSet.create(DexTokenType.WS, DexTokenType.NLS);
  public static final TokenSet COMMENTS = TokenSet.create(DexTokenType.LINE_COMMENT, DexTokenType.MULTILINE_COMMENT);
  public static final TokenSet STRING_LITERALS = TokenSet.create(STRING, RAW_STRING, CHAR);

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new DexLexer();
  }

  @NotNull
  @Override
  public PsiParser createParser(Project project) {
    return new DexParser();
  }

  @NotNull
  @Override
  public IFileElementType getFileNodeType() {
    return DexFileElementType.INSTANCE;
  }

  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    return WHITESPACES;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return STRING_LITERALS;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    return Factory.createElement(node);
  }

  @NotNull
  @Override
  public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new DexFile(viewProvider);
  }

  @NotNull
  @Override
  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }
}
