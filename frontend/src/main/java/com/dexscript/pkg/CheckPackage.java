package com.dexscript.pkg;


import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.shim.OutShim;
import com.dexscript.type.composite.ActorType;
import com.dexscript.type.composite.GlobalSPI;
import com.dexscript.type.composite.InterfaceType;
import com.dexscript.type.core.TypeSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.dexscript.pkg.DexPackages.$p;

public class CheckPackage {

    static class Failed extends RuntimeException {
    }

    public static boolean $(Supplier<ImportPackage.Impl> implSupplier, String pathStr) {
        try {
            return check(implSupplier, pathStr);
        } catch (DexSyntaxException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Failed e) {
            return false;
        }
    }

    private static boolean check(Supplier<ImportPackage.Impl> implSupplier, String pathStr) {
        List<DexFile> dexFiles = new ArrayList<>();
        Path pkgPath = $p(pathStr);
        if (!Files.isDirectory(pkgPath)) {
            System.out.println("package directory not found: " + pkgPath);
            return false;
        }
        ImportPackage.Impl impl = implSupplier.get();
        DexPackage pkg = impl.pkg(pkgPath);
        try {
            Files.list(pkgPath).forEach(path -> {
                try {
                    Text src = new Text(Files.readAllBytes(path));
                    assertNotBlank(path, src);
                    DexFile dexFile = new DexFile(src, path.getFileName().toString());
                    dexFile.attach(pkg);
                    dexFiles.add(dexFile);
                } catch (IOException e) {
                    System.out.println("failed to read file: " + path);
                    throw new Failed();
                }
            });
        } catch (IOException e) {
            System.out.println("failed to list package dir: " + pkgPath);
            throw new Failed();
        }
        if (hasSyntaxError(dexFiles)) {
            return false;
        }
        if (hasSemanticError(impl, dexFiles)) {
            return false;
        }
        return true;
    }

    private static void assertNotBlank(Path path, Text src) {
        for (int i = 0; i < src.end; i++) {
            if (Blank.$(src.bytes[i])) {
                continue;
            }
            return;
        }
        System.out.println(path + " is blank");
        throw new Failed();
    }

    private static boolean hasSemanticError(ImportPackage.Impl impl, List<DexFile> dexFiles) {
        boolean foundSpi = false;
        for (DexFile dexFile : dexFiles) {
            if (dexFile.fileName().equals("__spi__.ds")) {
                defineSpiFile(impl, dexFile);
                foundSpi = true;
            } else {
                defineNormalFile(impl, dexFile);
            }
        }
        if (!foundSpi) {
            System.out.println("missing __spi__.ds");
            return true;
        }
        for (DexFile dexFile : dexFiles) {
            if (new CheckSemanticError(impl.typeSystem(), dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }

    private static void defineNormalFile(ImportPackage.Impl impl, DexFile dexFile) {
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.inf() != null) {
                if (topLevelDecl.inf().isGlobalSPI()) {
                    System.out.println("can not define interface :: in file other than __spi__.ds");
                    throw new Failed();
                }
                new InterfaceType(impl.typeSystem(), topLevelDecl.inf());
            } else if (topLevelDecl.actor() != null) {
                new ActorType(impl, topLevelDecl.actor());
            }
        }
    }

    private static void defineSpiFile(ImportPackage.Impl impl, DexFile dexFile) {
        TypeSystem ts = impl.typeSystem();
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.actor() != null) {
                System.out.println("can not define function in __spi__.ds");
                throw new Failed();
            }
            DexInterface inf = topLevelDecl.inf();
            if (inf != null) {
                if (inf.isGlobalSPI()) {
                    new GlobalSPI(ts, inf);
                } else {
                    new InterfaceType(ts, inf);
                }
            }
        }
    }

    private static boolean hasSyntaxError(List<DexFile> dexFiles) {
        for (DexFile dexFile : dexFiles) {
            dexFile.topLevelDecls();
            if (new CheckSyntaxError(dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }
}
