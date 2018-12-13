package com.dexscript.transpile.type;

import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeCandidates {

    private final List<TypeCandidate> candidates = new ArrayList<>();
    private final OutShim oShim;

    public TypeCandidates(OutShim oShim) {
        this.oShim = oShim;
    }

    public String genIsF(Type targetType) {
        return null;
    }
}
