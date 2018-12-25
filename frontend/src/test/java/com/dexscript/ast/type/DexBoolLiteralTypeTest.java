package com.dexscript.ast.type;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.CoreTextContentNodeRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DexBoolLiteralTypeTest {

    @Test
    public void matched() {
        Assert.assertEquals("true", new DexBoolLiteralType("true").toString());
    }

    @Test
    public void parse_markdown() throws IOException {
        String path = "/"  +getClass().getCanonicalName().replace(".", "/") + ".md";
        System.out.println(path);
        InputStream resourceAsStream = getClass().getResourceAsStream(path);
        Parser parser = Parser.builder().build();
        Node node = parser.parseReader(new InputStreamReader(resourceAsStream));
        node.accept(new AbstractVisitor() {
            @Override
            public void visit(Heading heading) {
            }

            @Override
            public void visit(Text text) {
                System.out.println(text);
            }
        });
    }
}
