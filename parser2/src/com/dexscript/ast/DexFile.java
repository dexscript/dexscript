package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Text;

import java.util.ArrayList;
import java.util.List;

public final class DexFile extends DexElement {

    private final String fileName;
    private DexPackageClause packageClause;
    private List<DexRootDecl> rootDecls;

    public DexFile(Text src, String fileName) {
        super(src);
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
        packageClause.reparent(this);
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

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return src.end;
    }

    @Override
    public boolean matched() {
        return true;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (packageClause() != null) {
            visitor.visit(packageClause());
        }
        if (rootDecls() != null) {
            for (DexRootDecl rootDecl : rootDecls()) {
                visitor.visit(rootDecl);
            }
        }
    }
}
