package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Text;

import java.util.ArrayList;
import java.util.List;

public class DexFile implements DexElement  {

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
            DexRootDecl rootDecl = DexRootDecl.parse(remaining);
            if (rootDecl instanceof DexFunction) {
                ((DexFunction)rootDecl).reparent(this);
            }
            if (rootDecl.matched()) {
                rootDecls.add(rootDecl);
            } else {
                return rootDecls;
            }
            remaining = src.slice(rootDecl.end());
        }
    }

    @Override
    public Text src() {
        return src;
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
    public DexError err() {
        return null;
    }

    @Override
    public DexElement parent() {
        return null;
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
