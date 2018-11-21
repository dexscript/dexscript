package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.stmt.DexIdentifier;

public class DexPackageClause {

    private final Text src;
    private DexPackageKeyword packageKeyword;
    private DexIdentifier identifier;

    public DexPackageClause(Text src) {
        this.src = src;
    }

    public DexPackageKeyword packageKeyword() {
        if (packageKeyword != null) {
            return packageKeyword;
        }
        packageKeyword = new DexPackageKeyword(src);
        return packageKeyword;
    }

    public DexIdentifier identifier() {
        if (identifier != null) {
            return identifier;
        }
        DexPackageKeyword keyword = packageKeyword();
        identifier = new DexIdentifier(new Text(src.bytes, keyword.end(), src.end));
        return identifier;
    }
}
