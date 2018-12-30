package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.file.Files;

import static com.dexscript.pkg.DexPackages.$p;

public class TranspileContextTest {

    @Test
    public void use_context() throws Exception {
        Transpile.setup();
        Files.createDirectory($p("/pkg1"));
        Files.write($p("/pkg1/__spi__.ds"), ("" +
                "interface :: {\n" +
                "}\n" +
                "interface $ {\n" +
                "   GetPid(): string\n" +
                "}").getBytes());
        Files.write($p("/pkg1/123.ds"), ("" +
                "function Hello(): string {\n" +
                "   ctx := new Context()\n" +
                "   return F1($=ctx)\n" +
                "}\n" +
                "function Context() {\n" +
                "   await {\n" +
                "   case GetPid(): string {\n" +
                "       resolve 'hello' -> GetPid\n" +
                "   }}\n" +
                "}\n" +
                "function F1(): string {\n" +
                "   return F2()\n" +
                "}\n" +
                "function F2(): string {\n" +
                "   return $.GetPid()\n" +
                "}\n").getBytes());
        OutTown oTown = new OutTown();
        oTown.importPackage("/pkg1");
        Class shimClass = oTown.transpile();
        Method newHello = shimClass.getMethod("Hello");
        Assert.assertEquals("hello", newHello.invoke(null));
    }
}
