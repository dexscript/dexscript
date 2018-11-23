package com.dexscript.resolve;

import java.util.HashMap;
import java.util.Map;

public class DenotationTable extends HashMap<String, Denotation> {

    public DenotationTable() {
    }

    public DenotationTable(DenotationTable that) {
        super(that);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Denotation> m) {
        if (m == null) {
            return;
        }
        super.putAll(m);
    }

    public DenotationTable copy() {
        return new DenotationTable(this);
    }

    public DenotationTable add(Denotation denotation) {
        put(denotation.name, denotation);
        return this;
    }
}
