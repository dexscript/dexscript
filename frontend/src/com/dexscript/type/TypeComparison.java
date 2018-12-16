package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TypeComparison {

    private final DType to;
    private final DType from;

    public TypeComparison(DType to, DType from) {
        this.to = to;
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeComparison that = (TypeComparison) o;
        return Objects.equals(to, that.to) &&
                Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, from);
    }

    public boolean isAssignable() {
        ArrayList<String> logs = new ArrayList<>();
        TypeComparisonContext ctx = new TypeComparisonContext(new HashMap<>())
                .logUntilLevelN(4)
                .logCollector(logs);
        boolean assignableFrom = to.isAssignableFrom(ctx, from);
        for (String log : logs) {
            System.out.println(log);
        }
        return assignableFrom;
    }

    @Override
    public String toString() {
        return to + " | " + from;
    }
}
