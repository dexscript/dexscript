package com.dexscript.resolve;

import java.util.Objects;

public class Pier {

    private final String name;
    private final int argsCount;

    public Pier(String name, int argsCount) {
        this.name = name;
        this.argsCount = argsCount;
    }

    public String name() {
        return name;
    }

    public int argsCount() {
        return argsCount;
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
