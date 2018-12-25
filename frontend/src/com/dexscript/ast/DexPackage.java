package com.dexscript.ast;

public class DexPackage {

    public final static DexPackage DUMMY = new DexPackage("DUMMY__");

    private final String packageName;

    public DexPackage(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return packageName;
    }
}
