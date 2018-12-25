package com.dexscript.ast.type;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.io.InputStream;
import java.io.InputStreamReader;

public interface LoadTestData {

    static Node $(Class clazz) {
        try {
            String path = "/" + clazz.getCanonicalName().replace(".", "/") + ".md";
            Parser parser = Parser.builder().build();
            try (InputStream inputStream = clazz.getResourceAsStream(path)) {
                try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                    return parser.parseReader(reader);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
