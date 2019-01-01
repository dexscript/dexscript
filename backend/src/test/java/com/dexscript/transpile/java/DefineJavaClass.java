package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public interface DefineJavaClass {
    static Map<String, Class<?>> $(OutTown oTown) {
        try {
            List<String> javaCodes = testDataFromMySection().codes("java");
            Pattern packageNamePattern = Pattern.compile("package\\s+(.*)\\s*\\;");
            Pattern classNamePattern = Pattern.compile("class\\s+([A-Za-z][A-Za-z0-9_]*)\\s*");
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
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
