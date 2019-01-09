package com.dexscript.pkg;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.type.composite.ActorType;
import com.dexscript.type.composite.InterfaceType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.dexscript.pkg.DexPackages.$p;

public class ImportPackage {

    public static ImportPackage IMPORT_PACKAGE = new ImportPackage();

    private ImportPackage() {
    }

    public interface Impl extends ActorType.Impl {
        DexPackage pkg(Path pkgPath);
    }

    public void $(Impl impl, String pkgPathStr) {
        ArrayList<DexFile> dexFiles = new ArrayList<>();
        Path pkgPath = $p(pkgPathStr);
        DexPackage pkg = impl.pkg(pkgPath);
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
                    new ActorType(impl, topLevelDecl.actor());
                } else if (topLevelDecl.inf() != null) {
                    if (topLevelDecl.inf().isGlobalSPI()) {
                        continue;
                    }
                    new InterfaceType(impl.typeSystem(), topLevelDecl.inf());
                }
            }
        }
    }
}
