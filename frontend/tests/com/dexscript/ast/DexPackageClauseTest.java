package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexPackageClauseTest {

    @Test
    public void matched() {
        DexPackageClause pkg = new DexPackageClause(" package example");
        Assert.assertEquals("example", pkg.identifier().toString());
        Assert.assertEquals("package example", pkg.toString());
    }

    @Test
    public void without_space() {
        DexPackageClause pkg = new DexPackageClause("packageabc");
        Assert.assertEquals("<unmatched>packageabc</unmatched>", pkg.toString());
    }

    @Test
    public void invalid_identifier_recover_by_file_end() {
        DexPackageClause pkg = new DexPackageClause(" package ?");
        Assert.assertEquals("<unmatched>?</unmatched>", pkg.identifier().toString());
        Assert.assertEquals("package <error/>?", pkg.toString());
    }

    @Test
    public void invalid_identifier_recover_by_semicolon() {
        DexPackageClause pkg = new DexPackageClause(" package ?;example");
        Assert.assertEquals("<unmatched>?;example</unmatched>", pkg.identifier().toString());
        Assert.assertEquals("package <error/>?", pkg.toString());
    }

    @Test
    public void garbage_in_prelude() {
        DexPackageClause pkg = new DexPackageClause("ddd package example");
        Assert.assertEquals("example", pkg.identifier().toString());
        Assert.assertEquals("<error/>package example", pkg.toString());
    }

    @Test
    public void garbage_in_prelude_without_space() {
        DexPackageClause pkg = new DexPackageClause("dddpackage example");
        Assert.assertEquals("<unmatched>dddpackage example</unmatched>", pkg.toString());
    }
}
