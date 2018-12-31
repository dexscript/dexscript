package com.dexscript.transpile.java;

import com.dexscript.test.framework.FluentAPI;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.Transpile;
import org.junit.Test;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class CallJavaFunctionTest {

    @Test
    public void call_java_function() throws Exception {
        OutTown oTown = new OutTown();
        FluentAPI testData = testDataFromMySection();
        for (Class<?> clazz : javaClassesFromMySection(oTown).values()) {
            oTown.oShim().importJavaFunctions(clazz);
        }
        List<String> codes = testData.codes("dexscript");
        testData.assertByList(Transpile.$(oTown, codes.get(0), codes.get(1)));
    }

    public static Map<String, Class<?>> javaClassesFromMySection(OutTown oTown) throws Exception {
        List<String> javaCodes = testDataFromMySection().codes("java");
        Pattern packageNamePattern = Pattern.compile("package\\s+(.*)\\s*\\;");
        Pattern classNamePattern = Pattern.compile("class\\s+([A-Za-z][A-Za-z0-9_]*)\\s*\\{");
        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        for (String javaCode : javaCodes) {
            Matcher matcher = packageNamePattern.matcher(javaCode);
            if (!matcher.find()) {
                throw new RuntimeException("invalid java code: missing package name");
            }
            String packageName = matcher.group(1);
            matcher = classNamePattern.matcher(javaCode);
            if (!matcher.find()) {
                throw new RuntimeException("invalid java code: missing class name");
            }
            String className = matcher.group(1);
            oTown.addSource(packageName + "." + className, javaCode);
            compiler.addSource(packageName + "." + className, javaCode);
        }
        return compiler.compileAll();
    }
}
