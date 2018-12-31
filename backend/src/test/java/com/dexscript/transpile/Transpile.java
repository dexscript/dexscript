package com.dexscript.transpile;

import com.dexscript.pkg.DexPackages;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dexscript.pkg.DexPackages.$p;

public interface Transpile {

    static Object $(String src) {
        OutTown oTown = new OutTown();
        return Transpile.$(oTown, null, src);
    }

    static Object $(OutTown oTown, String spiSrc, String implSrc) {
        try {
            setup();
            Files.createDirectory($p("/pkg1"));
            if (spiSrc == null) {
                Files.write($p("/pkg1/__spi__.ds"), ("" +
                        "interface :: {\n" +
                        "}").getBytes());
            } else {
                Files.write($p("/pkg1/__spi__.ds"), spiSrc.getBytes());
            }
            Files.write($p("/pkg1/hello.ds"), implSrc.getBytes());
            Class shimClass = oTown
                    .importPackage("/pkg1")
                    .transpile();
            Method newHello = shimClass.getMethod("Hello");
            return newHello.invoke(null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void setup() {
        OutTown.ON_SOURCE_ADDED = (className, classSrc) -> {
            System.out.println(">>> " + className);
            String lines[] = classSrc.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                System.out.println((i + 1) + ":\t" + line);
            }
            writeToFile(className, classSrc);
        };
        DexPackages.fs = Jimfs.newFileSystem(Configuration.unix());
    }

    static void writeToFile(String className, String classSrc) {
        try {
            Path path = Paths.get("/tmp/dexscript/" + className.replace(".", "/") + ".java");
            Files.createDirectories(path.getParent());
            Files.write(path, classSrc.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
