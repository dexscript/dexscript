package com.dexscript.pkg;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static com.dexscript.pkg.DexPackages.$p;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class CheckPackageTest {

    @Before
    public void setup() {
        DexPackages.fs = Jimfs.newFileSystem(Configuration.unix());
    }

    @Test
    public void no_error() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void package_dir_not_found() {
        Assert.assertFalse(CheckPackage.$(FakeImportPackageImpl::new, "/pkg1"));
    }

    @Test
    public void spi_file_not_found() throws IOException {
        Files.createDirectory($p("/pkg1"));
        Assert.assertFalse(CheckPackage.$(FakeImportPackageImpl::new,"/pkg1"));
    }

    @Test
    public void syntax_error() throws IOException {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void referenced_not_existing_function() throws IOException {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void reference_function_defined_by_global_spi() throws IOException {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void global_spi_must_be_defined_in_spi_file() throws IOException {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void can_not_define_type_with_same_name() throws IOException {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void can_not_define_same_name_function_in_different_file() throws IOException {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void global_spi_reference_later_defined_types() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void invoke_generic_method() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void function_call_can_not_be_left_value_of_assignment() throws Exception {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void array_support_is_builtin() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void double_colon_function_call_can_not_reference_locally() throws Exception {
        Assert.assertFalse(checkPkg1());
    }

    @Test
    public void double_colon_function_call_can_reference_global_spi() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void double_colon_value_reference_global_spi() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void array_of_compatible_type_is_assignable()  throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    @Test
    public void return_struct() throws Exception {
        Assert.assertTrue(checkPkg1());
    }

    private boolean checkPkg1() throws IOException {
        for (String code : testDataFromMySection().codes()) {
            int newLinePos = code.indexOf("\n");
            String filePath = code.substring(2, newLinePos).trim();
            Files.createDirectories($p(filePath).getParent());
            Files.write($p(filePath), code.substring(newLinePos).getBytes());
        }
        return CheckPackage.$(FakeImportPackageImpl::new, "/pkg1");
    }
}
