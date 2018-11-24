package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexReturnStmt;
import org.junit.Assert;
import org.junit.Test;

public class ResolveValueTest {

    @Test
    public void resolve_argument() {
        String src = "" +
                "function hello(msg: string): string {\n" +
                "   return msg;\n" +
                "}";
        DexReturnStmt stmt = (DexReturnStmt) new DexFunction(src).block().stmts().get(0);
        Denotation resolved = new ResolveValue().__((DexReference) stmt.expr());
        Assert.assertNotNull(resolved);
    }

    @Test
    public void resolve_function() {
        String src = "" +
                "package abc\n" +
                "function hello(): string {\n" +
                "   return A()\n" +
                "}\n" +
                "function A(): string{\n" +
                "   return 'a'\n" +
                "}";
        DexFile file = new DexFile(src);
        ResolveFunction resolveFunction = new ResolveFunction();
        resolveFunction.define(file);
        DexReference ref = file.rootDecls().get(0).asFunction().stmts().get(0)
                .asReturn().expr()
                .asCall().target().asRef();
        Denotation.Type type = resolveFunction.__(ref);
        Assert.assertEquals("String", type.ret.javaClassName);
    }
}
