package com.dexscript.ast;

import java.util.Objects;

public class DexPackage {

    // dummy package used for testing purpose only
    public final static DexPackage DUMMY = new DexPackage("DEXSCRIPT_DUMMY");
    public final static DexPackage BUILTIN = new DexPackage("DEXSCRIPT_BUILTIN");

    private final String packageName;

    public DexPackage(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DexPackage that = (DexPackage) o;
        return Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName);
    }
}
