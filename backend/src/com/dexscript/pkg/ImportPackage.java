package com.dexscript.pkg;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.shim.OutShim;

import java.util.ArrayList;

public class ImportPackage {
    public static void $(OutShim oShim, String pkgPathStr) {
        ArrayList<DexFile> dexFiles = new ArrayList<>();
        if (!CheckPackage.$(pkgPathStr, dexFiles)) {
            throw new DexSyntaxException("there is error in package: " + pkgPathStr);
        }
        for (DexFile dexFile : dexFiles) {
            for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
                if (topLevelDecl.actor() != null) {
                    oShim.defineActor(topLevelDecl.actor());
                } else if (topLevelDecl.inf() != null) {
                    oShim.defineInterface(topLevelDecl.inf());
                }
            }
        }
    }
}
