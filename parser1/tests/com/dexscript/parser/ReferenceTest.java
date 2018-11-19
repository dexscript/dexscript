package com.dexscript.parser;

import com.dexscript.psi.*;
import com.dexscript.psi.impl.DexReferenceExpressionImpl;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReferenceTest {

    private DexFileFactory dexFileFactory;

    @Before
    public void setup() {
        dexFileFactory = new DexFileFactory();
    }

    @After
    public void teardown() {
        dexFileFactory.close();
    }

    @Test
    public void test_reference_direct_argument() {
        String src = "" +
                "package abc\n" +
                "\n" +
                "function Hello(msg: string): string {\n" +
                "   return msg\n" +
                "}";
        DexFile iFile = dexFileFactory.createDexFile("hello.ds", src);
        List<PsiElement> elements = getElements(Arrays.asList(iFile.getChildren()),
                functionCalled("Hello"),
                statementsOfFunction(),
                exprReturned());
        Assert.assertEquals(1, elements.size());
        DexReferenceExpressionImpl iRefExpr = (DexReferenceExpressionImpl) elements.get(0);
        Assert.assertNotNull(iRefExpr.getReference().resolve());
    }

    private List<PsiElement> getElements(List<PsiElement> elements, DexExtractor... extractors) {
        for (DexExtractor extractor : extractors) {
            for (PsiElement element : elements) {
                element.accept(extractor);
            }
            if (extractor.extracted.isEmpty()) {
                throw new RuntimeException("matching elements not found");
            }
            elements = extractor.extracted;
            extractor.extracted = new ArrayList<>();
        }
        return elements;
    }

    private static abstract class DexExtractor extends DexVisitor {
        public List<PsiElement> extracted = new ArrayList<>();
    }

    private static DexExtractor functionCalled(String funcName) {
        return new DexExtractor() {
            @Override
            public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
                if (funcName.equals(iFuncDecl.getIdentifier().getNode().getText())) {
                    extracted.add(iFuncDecl);
                }
            }
        };
    }

    private static DexExtractor statementsOfFunction() {
        return new DexExtractor() {
            @Override
            public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
                for (DexStatement iStmt : iFuncDecl.getBlock().getStatementList()) {
                    extracted.add(iStmt);
                }
            }
        };
    }

    private static DexExtractor exprReturned() {
        return new DexExtractor() {
            @Override
            public void visitReturnStatement(@NotNull DexReturnStatement iReturnStmt) {
                for (DexExpression iExpr : iReturnStmt.getExpressionList()) {
                    extracted.add(iExpr);
                }
            }
        };
    }
}
