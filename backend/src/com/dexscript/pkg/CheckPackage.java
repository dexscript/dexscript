package com.dexscript.pkg;


import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.type.TypeSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dexscript.pkg.FS.$p;

public interface CheckPackage {

    class Failed extends RuntimeException {
    }

    static boolean $(String pathStr) throws IOException {
        Path pkgPath = $p(pathStr);
        if (!Files.isDirectory(pkgPath)) {
            System.out.println("package directory not found: " + pkgPath);
            return false;
        }
        if (hasSyntaxError(pkgPath)) {
            return false;
        }
        if (hasSemanticError(pkgPath)) {
            return false;
        }
        return true;
    }

    static boolean hasSemanticError(Path pkgPath) throws IOException {
        Path spiPath = $p(pkgPath.toString(), "__spi__.ds");
        if (!Files.isRegularFile(spiPath)) {
            System.out.println("package __spi__.ds not found: " + spiPath);
            return false;
        }
        TypeSystem ts = new TypeSystem();
        List<DexFile> dexFiles = new ArrayList<>();
        Files.list(pkgPath).forEach(path -> {
            try {
                Text src = new Text(Files.readAllBytes(path));
                DexFile dexFile = new DexFile(src, path.toString());
                dexFiles.add(dexFile);
                for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
                    if (topLevelDecl.inf() != null) {
                        ts.defineInterface(topLevelDecl.inf());
                    } else if (topLevelDecl.actor() != null) {
                    }
                }
            } catch (IOException e) {
                throw new Failed();
            }
        });
        for (DexFile dexFile : dexFiles) {
            if (new CheckSemanticError(ts, dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }

    static boolean hasSyntaxError(Path pkgPath) throws IOException {
        Optional<Path> hasSyntaxError = Files.list(pkgPath).filter(path -> {
            try {
                Text src = new Text(Files.readAllBytes(path));
                DexFile dexFile = new DexFile(src, path.toString());
                dexFile.topLevelDecls();
                if (new CheckSyntaxError(dexFile).hasError()) {
                    return true;
                }
            } catch (IOException e) {
                System.out.println("failed to read " + path + ": " + e);
                return true;
            }
            return false;
        }).findFirst();
        return hasSyntaxError.isPresent();
    }
}
