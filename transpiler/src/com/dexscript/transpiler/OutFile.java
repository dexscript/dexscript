package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OutFile extends DexVisitor {

    private final List<OutClass> oClasses = new ArrayList<>();
    private final DexFile iFile;
    private String packageName;

    OutFile(DexFile iFile) {
        this.iFile = iFile;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        packageName = o.getIdentifier().getNode().getText();
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
        oClasses.add(new OutRootClass(iFile, packageName, iFuncDecl));
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<OutClass> oClasses() {
        return Collections.unmodifiableList(oClasses);
    }

    public void genShim(InMemoryJavaCompiler compiler) {
        String shimClassName = oClasses.get(0).shimClassName();
        OutClass out = new OutClass(iFile, packageName, shimClassName);
        out.appendNewLine("import com.dexscript.runtime.Result;");
        out.appendNewLine();
        out.append("public class ");
        out.append(shimClassName);
        out.append(" {");
        out.indent(() -> {
            for (OutClass tClass : oClasses) {
                for (DexExpression ref : tClass.references()) {
                    if (ref instanceof DexCallExpr) {
                        DexCallExpr callExpr = (DexCallExpr) ref;
                        genShim4CallExpr(out, callExpr, callExpr.getExpression().getNode().getText());
                    } else if (ref instanceof DexNewExpr) {
                        DexNewExpr newExpr = (DexNewExpr) ref;
                        genShim4CallExpr(out, ref, newExpr.getExpression().getNode().getText());
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

    private void genShim4Add(OutClass out, DexAddExpr addExpr) {
        out.appendSourceLine(addExpr);
        out.appendNewLine("public static Result add(Object left, Object right) {");
        out.appendNewLine("  return com.dexscript.runtime.Math.add(left, right);");
        out.appendNewLine("}");
    }

    private void genShim4CallExpr(OutClass out, DexExpression callExpr, String funcName) {
        out.appendSourceLine(callExpr);
        out.append("public static Result ");
        out.append(funcName);
        out.append("() {");
        out.indent(() -> {
            out.append("return new ");
            out.append(funcName);
            out.append("();");
        });
        out.append("}");
    }
}
