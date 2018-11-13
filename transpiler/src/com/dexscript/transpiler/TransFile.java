package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class TransFile extends DexVisitor {

    private final List<TranspiledClass> tClasses = new ArrayList<>();
    private final String filename;
    private final String source;
    private String packageName;

    TransFile(String filename, String source) {
        this.filename = filename;
        this.source = source;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        packageName = o.getIdentifier().getNode().getText();
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration o) {
        TranspiledClass out = newTranspiledClass(packageName, o.getIdentifier().getNode().getText());
        out.appendNewLine("import com.dexscript.runtime.Actor;");
        out.appendNewLine("import com.dexscript.runtime.Result;");
        out.appendNewLine();
        out.appendSourceLine(o.getFunction());
        out.append("public class ");
        out.append(o.getIdentifier());
        out.append(" extends Actor {");
        out.indent(() -> {
            out.appendNewLine();
            // fields for return value
            DexType returnType = o.getSignature().getResult().getType();
            out.append("public ");
            out.append(returnType);
            out.append(" result1__;");
            out.appendNewLine();
            out.append("public Object result1__() {");
            out.indent(() -> {
                out.append("return result1__;");
            });
            out.append("}");
            out.appendNewLine();
            out.appendNewLine();
            // constructor
            out.append("public ");
            out.append(o.getIdentifier());
            out.append("() {");
            out.indent(() -> {
                o.getBlock().acceptChildren(new TransFunc(out, o));
            });
            out.append("}");
            out.appendNewLine();
            out.appendNewLine();
            for (Map.Entry<String, Integer> resultField : out.resultFieldNames().entrySet()) {
                out.append("private Result ");
                out.append(resultField.getKey());
                out.append(';');
                out.appendNewLine();
                if (resultField.getValue() > 1) {
                    throw new UnsupportedOperationException("not implemented yet");
                }
            }
        });
        out.append('}');
        out.appendNewLine();
    }

    @NotNull
    private TranspiledClass newTranspiledClass(String packageName, String className) {
        TranspiledClass out = new TranspiledClass(filename, source, packageName, className);
        tClasses.add(out);
        return out;
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<TranspiledClass> getTranspiledClasses() {
        return Collections.unmodifiableList(tClasses);
    }

    public void generateShim(InMemoryJavaCompiler compiler) {
        String shimClassName = tClasses.get(0).shimClassName();
        TranspiledClass out = new TranspiledClass(filename, source, packageName, shimClassName);
        out.appendNewLine("import com.dexscript.runtime.Result;");
        out.appendNewLine();
        out.append("public class ");
        out.append(shimClassName);
        out.append(" {");
        out.indent(() -> {
            for (TranspiledClass tClass : tClasses) {
                for (DexCallExpr ref : tClass.references()) {
                    out.appendSourceLine(ref);
                    out.append("public static Result ");
                    out.append(ref.getExpression().getNode().getText());
                    out.append("() {");
                    out.indent(() -> {
                        out.append("return new ");
                        out.append(ref.getExpression().getNode().getText());
                        out.append("();");
                    });
                    out.append("}");
                }
            }
        });
        out.append("}");
        out.appendNewLine();
        System.out.println(out.toString());
        out.addToCompiler(compiler);
    }
}
