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

package com.dexscript.stubs.types;

import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexNamedElement;
import com.dexscript.psi.impl.DexFunctionDeclarationImpl;
import com.dexscript.stubs.DexFunctionDeclarationStub;
import com.dexscript.stubs.index.DexFunctionIndex;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ArrayFactory;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DexFunctionDeclarationStubElementType extends DexNamedStubElementType<DexFunctionDeclarationStub, DexFunctionDeclaration> {
  public static final DexFunctionDeclaration[] EMPTY_ARRAY = new DexFunctionDeclaration[0];

  public static final ArrayFactory<DexFunctionDeclaration> ARRAY_FACTORY =
    count -> count == 0 ? EMPTY_ARRAY : new DexFunctionDeclaration[count];
  
  private static final ArrayList<StubIndexKey<String, ? extends DexNamedElement>> EXTRA_KEYS =
    ContainerUtil.newArrayList(DexFunctionIndex.KEY);

  public DexFunctionDeclarationStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public DexFunctionDeclaration createPsi(@NotNull DexFunctionDeclarationStub stub) {
    return new DexFunctionDeclarationImpl(stub, this);
  }

  @NotNull
  @Override
  public DexFunctionDeclarationStub createStub(@NotNull DexFunctionDeclaration psi, StubElement parentStub) {
    return new DexFunctionDeclarationStub(parentStub, this, psi.getName(), true);
  }

  @Override
  public void serialize(@NotNull DexFunctionDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isPublic());
  }

  @NotNull
  @Override
  public DexFunctionDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean());
  }

  @NotNull
  @Override
  protected Collection<StubIndexKey<String, ? extends DexNamedElement>> getExtraIndexKeys() {
    return EXTRA_KEYS;
  }
}
