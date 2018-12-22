package com.dexscript.pkg;


import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.dexscript.pkg.FS.$p;

public interface CheckPackage {

    static boolean $(String pathStr) throws IOException {
        Path pkgPath = $p(pathStr);
        if (!Files.isDirectory(pkgPath)) {
            System.out.println("package directory not found: " + pkgPath);
            return false;
        }
        Path spiPath = $p(pathStr, "__spi__.ds");
        if (!Files.isRegularFile(spiPath)) {
            System.out.println("package __spi__.ds not found: " + spiPath);
            return false;
        }
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
        if (hasSyntaxError.isPresent()) {
            return false;
        }
        return true;
    }
}
