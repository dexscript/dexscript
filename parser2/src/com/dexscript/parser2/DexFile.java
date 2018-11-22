package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

import java.util.List;

public class DexFile {

    private final String fileName;
    private final Text src;
    private DexPackageClause packageClause;
    private List<DexRootDecl> rootDecls;

    public DexFile(Text src, String fileName) {
        this.src = src;
        this.fileName = fileName;
    }

    public DexFile(Text src) {
        this(src, "");
    }

    public DexFile(String src) {
        this(new Text(src.getBytes(), 0, src.getBytes().length));
    }

    public String fileName() {
        return fileName;
    }

    public DexPackageClause packageClause() {
        if (packageClause != null) {
            return packageClause;
        }
        packageClause = new DexPackageClause(src);
        return packageClause;
    }

    public List<DexRootDecl> rootDecls() {
        if (rootDecls != null) {
            return rootDecls;
        }
        rootDecls = DexRootDecl.parse(src);
        return rootDecls;
    }
}
