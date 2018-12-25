package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Text;

import java.util.ArrayList;
import java.util.List;

public final class DexFile extends DexElement {

    private DexSyntaxError syntaxError;
    private final String fileName;
    private List<DexTopLevelDecl> rootDecls;

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

    public List<DexTopLevelDecl> topLevelDecls() {
        if (rootDecls != null) {
            return rootDecls;
        }
        Text remaining = src;
        rootDecls = new ArrayList<>();
        while (true) {
            DexTopLevelDecl rootDecl = new DexTopLevelDecl(remaining);
            rootDecl.reparent(this);
            if (rootDecl.matched()) {
                rootDecls.add(rootDecl);
            } else {
                if (rootDecls.isEmpty()) {
                    syntaxError = new DexSyntaxError(src, 0);
                }
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
        if (topLevelDecls() != null) {
            for (DexTopLevelDecl rootDecl : topLevelDecls()) {
                visitor.visit(rootDecl);
            }
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }
}
