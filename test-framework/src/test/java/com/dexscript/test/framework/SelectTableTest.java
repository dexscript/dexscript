package com.dexscript.test.framework;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SelectTableTest {
    @Test
    public void test() {
        FluentAPI fluentAPI = TestFramework.testDataFrom(getClass());
        List<Table> tables = fluentAPI.tables();
        Assert.assertEquals(1, tables.size());
        Table table = tables.get(0);
        Assert.assertEquals(Arrays.asList("name", "age", "hobby"), table.head);
        Assert.assertEquals(2, table.body.size());
    }
}