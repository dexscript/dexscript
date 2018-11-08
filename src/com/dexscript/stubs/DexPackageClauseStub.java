package com.dexscript.stubs;

import com.dexscript.psi.DexPackageClause;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

public class DexPackageClauseStub extends StubBase<DexPackageClause> {


    private final String myName;

    public DexPackageClauseStub(StubElement parent, IStubElementType elementType, String name) {
        super(parent, elementType);
        myName = name;
    }

    public DexPackageClauseStub(StubElement stub, IStubElementType elementType, StringRef ref) {
        super(stub, elementType);
        myName = ref != null ? ref.getString() : null;
    }

    public String getName() {
        return myName;
    }
}
