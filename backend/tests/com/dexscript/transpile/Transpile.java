package com.dexscript.transpile;

import com.dexscript.pkg.Package;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.dexscript.pkg.Package.$p;

public class Transpile {

    public static Object $(String src, Object... args) {
        OutTown oTown = new OutTown();
        return Transpile.$(oTown, src, args);
    }

    public static Object $(OutTown oTown, String src, Object... args) {
        try {
            setup();
            Files.createDirectory($p("/pkg1"));
            Files.write($p("/pkg1/__spi__.ds"), ("" +
                    "interface :: {\n" +
                    "}").getBytes());
            Files.write($p("/pkg1/hello.ds"), src.getBytes());
            Class shimClass = oTown
                    .importPackage("/pkg1")
                    .transpile();
            Method newHello = shimClass.getMethod("Hello");
            return newHello.invoke(null, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setup() {
        OutTown.ON_SOURCE_ADDED = (className, classSrc) -> {
            System.out.println(">>> " + className);
            String lines[] = classSrc.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                System.out.println((i + 1) + ":\t" + line);
            }
            writeToFile(className, classSrc);
        };
        Package.fs = Jimfs.newFileSystem(Configuration.unix());
    }

    private static void writeToFile(String className, String classSrc) {
        try {
            Path path = Paths.get("/tmp/dexscript/" + className.replace(".", "/") + ".java");
            Files.createDirectories(path.getParent());
            Files.write(path, classSrc.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
