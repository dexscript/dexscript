package com.dexscript.transpile.demo;

import com.dexscript.test.framework.FluentAPI;
import com.dexscript.transpile.TestTranspile;

import static com.dexscript.test.framework.TestFramework.selectSection;
import static com.dexscript.test.framework.TestFramework.testDataFrom;

public class Fib {

    public static void main(String[] args) {
        FluentAPI testData = testDataFrom(Fib.class);
        testData.select(selectSection("print_fib"))
                .assertByList(TestTranspile::$);
    }
}
