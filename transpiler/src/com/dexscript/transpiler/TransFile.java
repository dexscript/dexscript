package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            genReturnValueFields(out, o);
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
            for (TranspiledField field : out.fields()) {
                out.append("private ");
                out.append(field.type);
                out.append(' ');
                out.append(field.transpiledName);
                out.appendNewLine(';');
            }
        });
        out.append('}');
        out.appendNewLine();
    }

    private void genReturnValueFields(TranspiledClass out, @NotNull DexFunctionDeclaration o) {
        DexResult result = o.getSignature().getResult();
        if (result == null) {
            return;
        }
        DexType returnType = result.getType();
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

    public void genShim(InMemoryJavaCompiler compiler) {
        String shimClassName = tClasses.get(0).shimClassName();
        TranspiledClass out = new TranspiledClass(filename, source, packageName, shimClassName);
        out.appendNewLine("import com.dexscript.runtime.Result;");
        out.appendNewLine();
        out.append("public class ");
        out.append(shimClassName);
        out.append(" {");
        out.indent(() -> {
            for (TranspiledClass tClass : tClasses) {
                for (DexExpression ref : tClass.references()) {
                    if (ref instanceof DexCallExpr) {
                        genShim4CallExpr(out, (DexCallExpr) ref);
                    } else if (ref instanceof DexAddExpr) {
                        genShim4Add(out, (DexAddExpr) ref);
                    } else {
                        throw new UnsupportedOperationException("not implemented");
                    }
                }
            }
        });
        out.append("}");
        out.appendNewLine();
        System.out.println(out.toString());
        out.addToCompiler(compiler);
    }

    private void genShim4Add(TranspiledClass out, DexAddExpr addExpr) {
        out.appendSourceLine(addExpr);
        out.appendNewLine("public static Result add(Object left, Object right) {");
        out.appendNewLine("  return com.dexscript.runtime.Math.add(left, right);");
        out.appendNewLine("}");
    }

    private void genShim4CallExpr(TranspiledClass out, DexCallExpr callExpr) {
        out.appendSourceLine(callExpr);
        out.append("public static Result ");
        out.append(callExpr.getExpression().getNode().getText());
        out.append("() {");
        out.indent(() -> {
            out.append("return new ");
            out.append(callExpr.getExpression().getNode().getText());
            out.append("();");
        });
        out.append("}");
    }
}
