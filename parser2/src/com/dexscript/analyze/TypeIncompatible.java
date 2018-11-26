package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.DexSemanticError;

public class TypeIncompatible implements DexSemanticError {

    private final DexElement occurredAt;
    private final Denotation.Type expectedType;
    private final Denotation.Type actualType;

    public TypeIncompatible(DexElement occurredAt, Denotation.Type expectedType, Denotation.Type actualType) {
        this.occurredAt = occurredAt;
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    @Override
    public DexElement occurredAt() {
        return occurredAt;
    }

    public Denotation.Type expectedType() {
        return expectedType;
    }

    public Denotation.Type actualType() {
        return actualType;
    }

    @Override
    public String toString() {
        return "can not assign " + actualType + " to " + expectedType;
    }
}
