package com.dexscript.transpile;

import com.dexscript.shim.OutShim;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public interface Transpile {

    static Object $(String src, Object... args) {
        OutTown oTown = new OutTown();
        return Transpile.$(oTown, src, args);
    }

    static Object $(OutTown oTown, String src, Object... args) {
        try {
            oTown.ON_SOURCE_ADDED = (className, classSrc) -> {
                System.out.println(">>> " + className);
                String lines[] = classSrc.split("\\r?\\n");
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    System.out.println((i + 1) + ":\t" + line);
                }
                writeToFile(className, classSrc);
            };
            Map<String, Class<?>> classes = oTown
                    .addFile("hello.ds", src)
                    .transpile();
            Class<?> shimClass = classes.get(OutShim.QUALIFIED_CLASSNAME);
            Method newHello = shimClass.getMethod("Hello");
            return newHello.invoke(null, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
