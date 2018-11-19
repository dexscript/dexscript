package com.dexscript.parser2;

public class DexFile {

    private final Text src;
    private DexPackageClause packageClause;

    public DexFile(Text src) {
        this.src = src;
    }

    public DexFile(String src) {
        this(new Text(src.getBytes(), 0, src.getBytes().length));
    }

    public DexPackageClause packageClause() {
        if (packageClause != null) {
            return packageClause;
        }
        packageClause = new DexPackageClause(src);
        return packageClause;
    }
}
