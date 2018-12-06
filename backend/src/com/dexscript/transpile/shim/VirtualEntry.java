package com.dexscript.transpile.shim;

import java.util.Objects;

class VirtualEntry {

    private final String funcName;
    private final int paramsCount;

    public VirtualEntry(String funcName, int paramsCount) {
        this.funcName = funcName;
        this.paramsCount = paramsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualEntry that = (VirtualEntry) o;
        return paramsCount == that.paramsCount &&
                Objects.equals(funcName, that.funcName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, paramsCount);
    }

    public String funcName() {
        return funcName;
    }

    public int paramsCount() {
        return paramsCount;
    }
}
