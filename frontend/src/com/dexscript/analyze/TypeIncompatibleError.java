package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.denotation.Type;
import com.dexscript.resolve.DexSemanticError;

public class TypeIncompatibleError implements DexSemanticError {

    private final DexElement occurredAt;
    private final Type expectedType;
    private final Type actualType;

    public TypeIncompatibleError(DexElement occurredAt, Type expectedType, Type actualType) {
        this.occurredAt = occurredAt;
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    @Override
    public DexElement occurredAt() {
        return occurredAt;
    }

    public Type expectedType() {
        return expectedType;
    }

    public Type actualType() {
        return actualType;
    }

    @Override
    public String toString() {
        return "can not assign " + actualType + " to " + expectedType + " @ " + occurredAt.parent();
    }
}
