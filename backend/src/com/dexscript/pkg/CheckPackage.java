package com.dexscript.pkg;


import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.actor.ActorType;
import com.dexscript.type.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.dexscript.pkg.Package.$p;

public class CheckPackage {

    static class Failed extends RuntimeException {
    }

    public static boolean $(String pathStr) {
        try {
            return check(pathStr);
        } catch (DexSyntaxException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Failed e) {
            return false;
        }
    }

    private static boolean check(String pathStr) {
        List<DexFile> dexFiles = new ArrayList<>();
        Path pkgPath = $p(pathStr);
        if (!Files.isDirectory(pkgPath)) {
            System.out.println("package directory not found: " + pkgPath);
            return false;
        }
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        Package pkg = new Package(oShim);
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
        if (hasSemanticError(oShim, dexFiles)) {
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

    private static boolean hasSemanticError(OutShim oShim, List<DexFile> dexFiles) {
        boolean foundSpi = false;
        for (DexFile dexFile : dexFiles) {
            if (dexFile.fileName().equals("__spi__.ds")) {
                defineSpiFile(oShim, dexFile);
                foundSpi = true;
            } else {
                defineNormalFile(oShim, dexFile);
            }
        }
        if (!foundSpi) {
            System.out.println("missing __spi__.ds");
            return true;
        }
        for (DexFile dexFile : dexFiles) {
            if (new CheckSemanticError(oShim.typeSystem(), dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }

    private static void defineNormalFile(OutShim oShim, DexFile dexFile) {
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.inf() != null) {
                if (topLevelDecl.inf().isGlobalSPI()) {
                    System.out.println("can not define interface :: in file other than __spi__.ds");
                    throw new Failed();
                }
                oShim.defineInterface(topLevelDecl.inf());
            } else if (topLevelDecl.actor() != null) {
                oShim.defineActor(topLevelDecl.actor());
            }
        }
    }

    private static void defineSpiFile(OutShim oShim, DexFile dexFile) {
        TypeSystem ts = oShim.typeSystem();
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.actor() != null) {
                System.out.println("can not define function in __spi__.ds");
                throw new Failed();
            }
            DexInterface inf = topLevelDecl.inf();
            if (inf != null) {
                if (inf.isGlobalSPI()) {
                    ts.defineGlobalSPI(inf);
                } else {
                    ts.defineInterface(inf);
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
