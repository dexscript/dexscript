package com.dexscript.pkg;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static com.dexscript.pkg.DexPackages.$p;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class CheckAssignmentTest {

    @Test
    public void valid_index_set() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void invalid_index_set() throws Exception {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void valid_field_set() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void invalid_field_set() throws Exception {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void valid_assignment() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void invalid_assignment() throws Exception {
        Assert.assertFalse(checkPkg1());
    }

    private boolean checkPkg1() throws IOException {
        DexPackages.fs = Jimfs.newFileSystem(Configuration.unix());
        Files.createDirectories($p("/pkg1"));
        Files.write($p("/pkg1/__spi__.ds"), "interface :: {}".getBytes());
        Files.write($p("/pkg1/123.ds"), testDataFromMySection().code().getBytes());
        return CheckPackage.$("/pkg1");
    }
}
