package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.dexscript.psi.impl.DexPsiImplUtil;

public class OutRootClass extends OutClass {

    private final Namer oGatewayNames = new Namer();
    private final DexFunctionDeclaration iFuncDecl;
    private int ctorParamsCount;

    public OutRootClass(DexFunctionDeclaration iFuncDecl, OutShim oShim) {
        super((DexFile) iFuncDecl.getContainingFile(), iFuncDecl.getIdentifier().getNode().getText(), oShim);
        this.iFuncDecl = iFuncDecl;
        ctorParamsCount = DexPsiImplUtil.getParamsCount(iFuncDecl.getSignature());
        String packageName = iFile().getPackageName();
        String className = iFuncDecl.getIdentifier().getNode().getText();
        append("package ");
        append(packageName);
        append(";\n\n");
        appendNewLine("import com.dexscript.runtime.*;");
        appendNewLine("import com.dexscript.gen.*;");
        appendNewLine();
        appendSourceLine(iFuncDecl.getFunction());
        append("public class ");
        append(className);
        append(" extends Actor implements Result");
        DexSignature iSig = iFuncDecl.getSignature();
        append(iSig.getResult() == null ? 0 : 1);
        append(" {");
        indent(() -> {
            appendNewLine();
            // oFields for return value
            appendReturnValueFields(iSig);
            appendNewLine();
            // constructor
            OutMethod oMethod = new OutMethod(this, iSig);
            oMethod.append("public ");
            oMethod.append(iFuncDecl.getIdentifier());
            oMethod.append('(');
            int paramsCount = DexPsiImplUtil.getParamsCount(iSig);
            oMethod.appendParamsDeclaration(paramsCount);
            oMethod.append(") {");
            oMethod.indent(() -> {
                iFuncDecl.getBlock().acceptChildren(oMethod);
            });
            oMethod.appendNewLine('}');
            genClassBody();
            addCtorBoat();
        });
        append('}');
        appendNewLine();
    }

    private void addCtorBoat() {
        addCtorBoatApply();
        addCtorBoatCheck();
        Pier pier = new Pier(className(), ctorParamsCount);
        Boat boat = new Boat(pier, qualifiedClassName(), "create__");
        oShim().addBoat(boat);
    }

    private void addCtorBoatCheck() {
        String checkFuncName = oGatewayNames.giveName("can__create__");
        append("public static boolean ");
        append(checkFuncName);
        append('(');
        appendParamsDeclaration(ctorParamsCount);
        appendNewLine(") {");
        appendNewLine("  return true;");
        appendNewLine("}");
    }

    private void addCtorBoatApply() {
        String applyFuncName = oGatewayNames.giveName("create__");
        append("public static Result ");
        append(applyFuncName);
        append('(');
        appendParamsDeclaration(ctorParamsCount);
        appendNewLine(") {");
        append("  return new ");
        append(packageName());
        append('.');
        append(className());
        append('(');
        appendParamsInvocation(ctorParamsCount);
        appendNewLine(");");
        appendNewLine("}");
    }

    @Override
    public void addServeBoat(String prefix, DexServeStatement iServeStmt) {
        String oMethodName = iServeStmt.getIdentifier().getNode().getText();
        int paramsCount = DexPsiImplUtil.getParamsCount(iServeStmt.getSignature());
        String applyFuncName = addServeBoatApply(prefix, oMethodName, paramsCount);
        addServeBoatCheck(prefix, applyFuncName, paramsCount);
        Pier pier = new Pier(oMethodName, paramsCount + 1);
        Boat boat = new Boat(pier, qualifiedClassName(), applyFuncName);
        oShim().addBoat(boat);
    }

    private String addServeBoatApply(String prefix, String oMethodName, int paramsCount) {
        String applyFuncName = oGatewayNames.giveName(oMethodName + "__");
        append("public static Result ");
        append(applyFuncName);
        append('(');
        appendParamsDeclaration(paramsCount + 1);
        appendNewLine(") {");
        append("  return ((");
        append(className());
        append(")arg1).");
        append(oMethodName);
        append('(');
        appendParamsInvocation(paramsCount);
        appendNewLine(");");
        appendNewLine("}");
        return applyFuncName;
    }

    private void addServeBoatCheck(String prefix, String applyFuncName, int paramsCount) {
        String checkFuncName = "can__" + applyFuncName;
        append("public static boolean ");
        append(checkFuncName);
        append('(');
        appendParamsDeclaration(paramsCount + 1);
        appendNewLine(") {");
        append("  return arg1.getClass() == ");
        append(className());
        appendNewLine(".class;");
        appendNewLine("}");
    }
}
