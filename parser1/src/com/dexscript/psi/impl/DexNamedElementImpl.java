/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
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
import com.dexscript.psi.DexNamedElement;
import com.dexscript.psi.DexType;
import com.dexscript.stubs.DexNamedStub;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class DexNamedElementImpl<T extends DexNamedStub<?>> extends DexStubbedElementImpl<T> implements DexCompositeElement, DexNamedElement {

  public DexNamedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public DexNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getIdentifier();
  }

  @Nullable
  @Override
  public String getName() {
    T stub = getStub();
    if (stub != null) {
      return stub.getName();
    }
    PsiElement identifier = getIdentifier();
    return identifier != null ? identifier.getText() : null;
  }
  
  @Nullable
  @Override
  public String getQualifiedName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTextOffset() {
    PsiElement identifier = getIdentifier();
    return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
  }

  @NotNull
  @Override
  public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public DexType getDexType(@Nullable ResolveState context) {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public DexType findSiblingType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ItemPresentation getPresentation() {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public GlobalSearchScope getResolveScope() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBlank() {
    return StringUtil.equals(getName(), "_");
  }

  @Override
  public boolean shouldGoDeeper() {
    return true;
  }
}
