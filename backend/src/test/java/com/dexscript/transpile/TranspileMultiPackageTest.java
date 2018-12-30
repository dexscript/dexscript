package com.dexscript.transpile;

import com.dexscript.pkg.DexPackages;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static com.dexscript.pkg.DexPackages.$p;

public class TranspileMultiPackageTest {

    @Test
    public void type_of_same_name_is_allowed_in_multi_package() throws IOException {
        DexPackages.fs = Jimfs.newFileSystem(Configuration.unix());
        Files.createDirectory($p("/pkg1"));
        Files.write($p("/pkg1/__spi__.ds"), ("" +
                "interface :: {\n" +
                "}\n" +
                "interface abc {\n" +
                "}").getBytes());
        Files.createDirectory($p("/pkg2"));
        Files.write($p("/pkg2/__spi__.ds"), ("" +
                "interface :: {\n" +
                "}\n" +
                "interface abc {\n" +
                "}").getBytes());
        OutTown oTown = new OutTown();
        oTown.importPackage("/pkg1");
        oTown.importPackage("/pkg2");
        oTown.transpile();
    }
}
