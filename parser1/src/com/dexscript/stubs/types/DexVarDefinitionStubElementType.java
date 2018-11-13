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
import com.dexscript.psi.DexVarDefinition;
import com.dexscript.psi.impl.DexVarDefinitionImpl;
import com.dexscript.stubs.DexVarDefinitionStub;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexVarDefinitionStubElementType extends DexNamedStubElementType<DexVarDefinitionStub, DexVarDefinition> {
    public static final DexVarDefinition[] EMPTY_ARRAY = new DexVarDefinition[0];

    public static final ArrayFactory<DexVarDefinition> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new DexVarDefinition[count];

    public DexVarDefinitionStubElementType(@NotNull String name) {
        super(name);
    }

    @NotNull
    @Override
    public DexVarDefinition createPsi(@NotNull DexVarDefinitionStub stub) {
        return new DexVarDefinitionImpl(stub, this);
    }

    @NotNull
    @Override
    public DexVarDefinitionStub createStub(@NotNull DexVarDefinition psi, StubElement parentStub) {
        return new DexVarDefinitionStub(parentStub, this, psi.getName(), true);
    }

    @Override
    public void serialize(@NotNull DexVarDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        dataStream.writeBoolean(stub.isPublic());
    }

    @NotNull
    @Override
    public DexVarDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new DexVarDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean());
    }

    @Override
    public boolean shouldCreateStub(@NotNull ASTNode node) {
        return super.shouldCreateStub(node) && PsiTreeUtil.getParentOfType(node.getPsi(), DexFunctionDeclaration.class) == null;
    }
}