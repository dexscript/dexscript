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

import com.dexscript.psi.DexImportSpec;
import com.dexscript.psi.impl.DexImportSpecImpl;
import com.dexscript.stubs.DexImportSpecStub;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexImportSpecStubElementType extends DexNamedStubElementType<DexImportSpecStub, DexImportSpec> {
  public static final DexImportSpec[] EMPTY_ARRAY = new DexImportSpec[0];
  public static final ArrayFactory<DexImportSpec> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new DexImportSpec[count];

  public DexImportSpecStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public DexImportSpec createPsi(@NotNull DexImportSpecStub stub) {
    return new DexImportSpecImpl(stub, this);
  }

  @NotNull
  @Override
  public DexImportSpecStub createStub(@NotNull DexImportSpec psi, StubElement parentStub) {
    return new DexImportSpecStub(parentStub, this, psi.getAlias(), psi.getPath(), psi.isDot());
  }

  @Override
  public void serialize(@NotNull DexImportSpecStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeUTFFast(StringUtil.notNullize(stub.getAlias()));
    dataStream.writeUTFFast(stub.getPath());
    dataStream.writeBoolean(stub.isDot());
  }

  @NotNull
  @Override
  public DexImportSpecStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexImportSpecStub(parentStub, this, StringUtil.nullize(dataStream.readUTFFast()), 
                                dataStream.readUTFFast(), dataStream.readBoolean());
  }

  @Override
  public boolean shouldCreateStub(@NotNull ASTNode node) {
    return true;
  }
}
