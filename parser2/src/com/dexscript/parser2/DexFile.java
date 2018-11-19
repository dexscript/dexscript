package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

import java.util.List;

public class DexFile {

    private final Text src;
    private DexPackageClause packageClause;
    private List<DexRootDeclaration> rootDeclarations;

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

    public List<DexRootDeclaration> rootDeclarations() {
        if (rootDeclarations != null) {
            return rootDeclarations;
        }
        rootDeclarations = DexRootDeclaration.parse(src);
        return rootDeclarations;
    }
}
