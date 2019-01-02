package com.dexscript.transpile;

import com.dexscript.test.framework.FluentAPI;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.file.Files;

import static com.dexscript.pkg.DexPackages.$p;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class ContextTest {

    @Test
    public void use_context() throws Exception {
        TestTranspile.setup();
        FluentAPI testData = testDataFromMySection();
        for (String code : testData.codes()) {
            int newLinePos = code.indexOf("\n");
            String filePath = code.substring(2, newLinePos).trim();
            Files.createDirectories($p(filePath).getParent());
            Files.write($p(filePath), code.substring(newLinePos).getBytes());
        }
        Class shimClass = new OutTown()
                .importPackage("/pkg1")
                .importPackage("/pkg2")
                .transpile();
        Method newHello = shimClass.getMethod("Hello");
        testData.assertByList(newHello.invoke(null));
    }
}
