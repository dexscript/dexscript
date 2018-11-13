package com.dexscript.parser;

import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class ParserTest extends ParsingTestCase {

    public ParserTest() {
        super("parser", "dex", new DexParserDefinition());
    }

    @Test
    public void testFunction() {
        doTest(true);
    }

    @Test
    public void testReturn() {
        doTest(true);
    }

    @Test
    public void testFunctionCall() {
        doTest(true);
    }

    @Test
    public void testPlus() {
        doTest(true);
    }

    @Test
    public void testAssignment() {
        doTest(true);
    }

    @Override
    protected void doTest(boolean checkErrors) {
        super.doTest(true);
        if (checkErrors) {
            assertFalse(
                    "PsiFile contains error elements",
                    toParseTreeText(myFile, skipSpaces(), includeRanges()).contains("PsiErrorElement")
            );
        }
    }
    @NotNull
    @Override
    protected String getTestDataPath() {
        return "testData";
    }
}
