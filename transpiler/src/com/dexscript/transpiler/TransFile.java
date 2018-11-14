package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TransFile extends DexVisitor {

    private final List<OutClass> tClasses = new ArrayList<>();
    private final DexFile iFile;
    private String packageName;

    TransFile(DexFile iFile) {
        this.iFile = iFile;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        packageName = o.getIdentifier().getNode().getText();
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
        OutClass oClass = newOutClass(packageName, iFuncDecl.getIdentifier().getNode().getText());
        oClass.appendNewLine("import com.dexscript.runtime.Actor;");
        oClass.appendNewLine("import com.dexscript.runtime.Result;");
        oClass.appendNewLine();
        oClass.appendSourceLine(iFuncDecl.getFunction());
        oClass.append("public class ");
        oClass.append(iFuncDecl.getIdentifier());
        oClass.append(" extends Actor {");
        oClass.indent(() -> {
            oClass.appendNewLine();
            // fields for return value
            genReturnValueFields(oClass, iFuncDecl);
            oClass.appendNewLine();
            // constructor
            OutMethod oMethod = new OutMethod(iFile);
            oMethod.append("public ");
            oMethod.append(iFuncDecl.getIdentifier());
            oMethod.append("() {");
            oMethod.indent(() -> {
                iFuncDecl.getBlock().acceptChildren(new TransFunc(oClass, oMethod, iFuncDecl));
            });
            oMethod.append("}");
            oClass.append(oMethod.toString());
            oClass.appendNewLine();
            oClass.appendNewLine();
            for (OutField field : oClass.fields()) {
                oClass.append("private ");
                oClass.append(field.type);
                oClass.append(' ');
                oClass.append(field.outName);
                oClass.appendNewLine(';');
            }
        });
        oClass.append('}');
        oClass.appendNewLine();
    }

    private void genReturnValueFields(OutClass out, @NotNull DexFunctionDeclaration o) {
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
    private OutClass newOutClass(String packageName, String className) {
        OutClass out = new OutClass(iFile, packageName, className);
        tClasses.add(out);
        return out;
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<OutClass> getTranspiledClasses() {
        return Collections.unmodifiableList(tClasses);
    }

    public void genShim(InMemoryJavaCompiler compiler) {
        String shimClassName = tClasses.get(0).shimClassName();
        OutClass out = new OutClass(iFile, packageName, shimClassName);
        out.appendNewLine("import com.dexscript.runtime.Result;");
        out.appendNewLine();
        out.append("public class ");
        out.append(shimClassName);
        out.append(" {");
        out.indent(() -> {
            for (OutClass tClass : tClasses) {
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
