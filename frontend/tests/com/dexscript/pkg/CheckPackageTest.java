package com.dexscript.pkg;

import org.junit.Test;

public class CheckPackageTest {

    @Test
    public void no_error() {
        CheckPackage.$("/v/pkg1");
    }
}
