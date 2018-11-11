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

package com.dexscript.psi.impl;

import com.dexscript.psi.DexCompositeElement;
import com.dexscript.psi.DexFile;
import com.dexscript.stubs.TextHolder;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DexStubbedElementImpl<T extends StubBase<?>> extends StubBasedPsiElementBase<T> implements DexCompositeElement {
  public DexStubbedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public DexStubbedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return getElementType().toString();
  }

  @Nullable
  @Override
  public String getText() {
    T stub = getStub();
    if (stub instanceof TextHolder) {
      String text = ((TextHolder)stub).getText();
      if (text != null) return text;
    }
    return super.getText();
  }

  @Override
  public PsiElement getParent() {
    return getParentByStub();
  }

  public DexFile getContainingFile() {
    return (DexFile)super.getContainingFile();
  }

  @Override
  public boolean shouldGoDeeper() {
    return true;
  }
}
