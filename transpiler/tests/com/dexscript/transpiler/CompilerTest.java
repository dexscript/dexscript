package com.dexscript.transpiler;

import org.junit.Test;
import org.mdkt.compiler.InMemoryJavaCompiler;

public class CompilerTest {

    @Test
    public void testCircularDependency() throws Exception {
        StringBuilder hello = new StringBuilder();
        hello.append("package org.mdkt;\n");
        hello.append("public class HelloClass {\n");
        hello.append("   public String hello() { return new WorldClass().hello(); }");
        hello.append("}");

        StringBuilder world = new StringBuilder();
        world.append("package org.mdkt;\n");
        world.append("public class WorldClass {\n");
        world.append("   public String hello() { return new HelloClass().hello(); }");
        world.append("}");

        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();
        compiler.addSource("org.mdkt.HelloClass", hello.toString());
        compiler.addSource("org.mdkt.WorldClass", world.toString());
        System.out.println(compiler.compileAll());
    }
}
