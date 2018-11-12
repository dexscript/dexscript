package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TransFile extends DexVisitor {

    private final List<TranspiledClass> classes = new ArrayList<>();
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
        out.append("import com.dexscript.runtime.Actor;\n");
        out.append("import com.dexscript.runtime.Result1Inf;\n");
        out.append("import com.dexscript.runtime.Ctor0Inf;\n");
        out.append("\n");
        out.appendSourceLine(o.getFunction());
        out.append("public class ");
        out.append(o.getIdentifier());
        out.append(" extends Actor implements Result1Inf {");
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
                out.appendNewLine("init__();");
                o.getBlock().acceptChildren(new TransFunc(out, o));
            });
            out.append("}");
        });
        out.endActorClass();
    }

    @NotNull
    private TranspiledClass newTranspiledClass(String packageName, String className) {
        TranspiledClass out = new TranspiledClass(filename, source, packageName, className);
        classes.add(out);
        return out;
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<TranspiledClass> getTranspiledClasses() {
        return Collections.unmodifiableList(classes);
    }

    public void generateBootstrapper(InMemoryJavaCompiler compiler) {
        String className = filename.replace(".", "__");
        TranspiledClass out = new TranspiledClass(filename, source, packageName, className);
        out.append("public class ");
        out.append(className);
        out.append(" {");
        out.indent(() -> {
            out.append("static {");
            out.indent(() -> {
                boolean isFirstLine = true;
                for (TranspiledClass clazz : classes) {
                    for (DexCallExpr ref : clazz.references()) {
                        if (isFirstLine) {
                            isFirstLine = false;
                        } else {
                            out.appendNewLine();
                        }
                        out.append(clazz.className());
                        out.append('.');
                        out.append(ref.getExpression());
                        out.append("__ctor = () -> new ");
                        out.append(ref.getExpression());
                        out.append("();");
                    }

                }
            });
            out.append("}");
        });
        out.append("}");
        out.appendNewLine();
        out.addToCompiler(compiler);
    }
}
