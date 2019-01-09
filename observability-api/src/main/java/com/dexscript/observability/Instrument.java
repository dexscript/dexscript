package com.dexscript.observability;

import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Join;
import com.dexscript.gen.Line;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class Instrument {

    private static final String PACKAGE_NAME = "com.dexscript.observability.gen";
    private static final String CLASS_NAME = "Instrumented";
    private static final String QUALIFIED_CLASS_NAME = PACKAGE_NAME + "." + CLASS_NAME;
    private static final Map<Class, Class> instrumenteds = new ConcurrentHashMap<>();

    static Class $(Class clazz) {
        Class instrumented = instrumenteds.get(clazz);
        if (instrumented != null) {
            return instrumented;
        }
        try {
            InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance().ignoreWarnings();
            instrumented = compiler.compile(QUALIFIED_CLASS_NAME, gen(clazz));
            instrumenteds.put(clazz, instrumented);
            return instrumented;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String gen(Class clazz) {
        Gen g = new Gen();
        g.__("package "
        ).__(PACKAGE_NAME
        ).__(new Line(";"));
        g.__(new Line("import java.util.*;"));
        g.__("public class "
        ).__(CLASS_NAME
        ).__(" implements "
        ).__(clazz.getCanonicalName()
        ).__(" {"
        ).__(new Indent(() -> genClassBody(g, clazz))
        ).__(new Line("}"));
        System.out.println(g.toString());
        return g.toString();
    }

    private static void genClassBody(Gen g, Class clazz) {
        Set<String> methodNames = new HashSet<>();
        for (Method method : clazz.getMethods()) {
            String id = assignId(methodNames, method.getName());
            String ns = clazz.getCanonicalName() + "." + id;
            g.__("private static final Map<String, String> "
            ).__(id
            ).__("_ATTRIBUTES = new HashMap<String, String>(){{"
            ).__(new Indent(() -> {
                    })
            ).__(new Line("}};"));
            g.__("private static final String[] "
            ).__(id
            ).__("_ARG_NAMES = new String[]{"
            ).__(new Join(", ", method.getParameters(), Parameter::getName)
            ).__(new Line("};"));
            g.__("public "
            ).__(method.getReturnType().getCanonicalName()
            ).__(" "
            ).__(method.getName()
            ).__("("
            ).__(new Join(", ", method.getParameters(), Instrument::formatParam)
            ).__(") {"
            ).__(new Indent(() -> genMethodBody(g, ns, id, method))
            ).__(new Line("}"));
        }
    }

    @NotNull
    private static String formatParam(Parameter param) {
        return param.getType().getCanonicalName() + " " + param.getName();
    }

    private static String assignId(Set<String> ids, String id) {
        if (!ids.contains(id)) {
            ids.add(id);
            return id;
        }
        for (int i = 2; ; i++) {
            String newId = id + i;
            if (!ids.contains(newId)) {
                ids.add(newId);
                return newId;
            }
        }
    }

    private static void genMethodBody(Gen g, String ns, String id, Method method) {
        g.__(Transaction.class.getCanonicalName()
        ).__(" tx = new "
        ).__(Transaction.class.getCanonicalName()
        ).__(new Line("();"));
        g.__("tx.ns = \""
        ).__(ns
        ).__(new Line("\";"));
        g.__("tx.attributes = "
        ).__(id
        ).__(new Line("_ATTRIBUTES;"));
        g.__("tx.argNames = "
        ).__(id
        ).__(new Line("_ARG_NAMES;"));
        g.__("tx.argValues = new Object[]{"
        ).__(new Join(", ", method.getParameters(), Parameter::getName)
        ).__(new Line("};"));
        g.__(Observability.class.getCanonicalName()
        ).__(new Line(".send(tx);"));
    }
}
