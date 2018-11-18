package com.dexscript.transpiler;

import java.util.Objects;

public class Pier {

    public final String name;
    public final int argsCount;

    public Pier(String name, int argsCount) {
        this.name = name;
        this.argsCount = argsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pier pier = (Pier) o;
        return argsCount == pier.argsCount &&
                Objects.equals(name, pier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argsCount);
    }
}
