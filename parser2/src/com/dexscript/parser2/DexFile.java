package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

import java.util.ArrayList;
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
        Text remaining = src;
        if (packageClause().matched()) {
            remaining = src.slice(packageClause().end());
        }
        rootDecls = new ArrayList<>();
        while (true) {
            DexRootDecl rootDecl = new DexRootDecl(remaining);
            rootDecl.reparent(this);
            if (rootDecl.matched()) {
                rootDecls.add(rootDecl);
            } else {
                return rootDecls;
            }
            remaining = src.slice(rootDecl.end());
        }
    }
}
