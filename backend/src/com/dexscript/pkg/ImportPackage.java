package com.dexscript.pkg;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.core.Text;
import com.dexscript.shim.OutShim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.dexscript.pkg.Package.$p;

public class ImportPackage {
    public static void $(OutShim oShim, String pkgPathStr) {
        if (!CheckPackage.$(pkgPathStr)) {
            throw new DexSyntaxException("there is error in package: " + pkgPathStr);
        }
        ArrayList<DexFile> dexFiles = new ArrayList<>();
        Path pkgPath = $p(pkgPathStr);
        Package pkg = new Package(oShim);
        try {
            Files.list(pkgPath).forEach(path -> {
                try {
                    Text src = new Text(Files.readAllBytes(path));
                    DexFile dexFile = new DexFile(src, path.getFileName().toString());
                    dexFile.attach(pkg);
                    dexFiles.add(dexFile);
                } catch (IOException e) {
                    System.out.println("failed to read file: " + path);
                }
            });
        } catch (IOException e) {
            System.out.println("failed to list package dir: " + pkgPath);
            return;
        }
        for (DexFile dexFile : dexFiles) {
            for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
                if (topLevelDecl.actor() != null) {
                    oShim.defineActor(topLevelDecl.actor());
                } else if (topLevelDecl.inf() != null) {
                    if (CheckPackage.isGlobalSpi(topLevelDecl)) {
                        continue;
                    }
                    oShim.defineInterface(topLevelDecl.inf());
                }
            }
        }
    }
}
