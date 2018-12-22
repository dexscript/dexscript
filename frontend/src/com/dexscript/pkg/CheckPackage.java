package com.dexscript.pkg;


import java.nio.file.Files;
import java.nio.file.Path;

import static com.dexscript.pkg.FS.$p;

public interface CheckPackage {

    static boolean $(String pathStr) {
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
        return true;
    }
}
