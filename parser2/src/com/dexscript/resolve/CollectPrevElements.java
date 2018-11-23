package com.dexscript.resolve;

import com.dexscript.ast.core.DexElement;

import java.util.ArrayList;
import java.util.List;

public class CollectPrevElements implements DexElement.Visitor {

    private final List<DexElement> collected = new ArrayList<>();

    public CollectPrevElements(DexElement elem) {
        elem.walkUp(this);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem == null) {
            return;
        }
        collected.add(elem);
        elem.walkUp(this);
    }

    public List<DexElement> collected() {
        return collected;
    }
}
