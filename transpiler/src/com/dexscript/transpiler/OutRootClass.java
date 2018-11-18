package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.dexscript.psi.impl.DexPsiImplUtil;

import java.util.List;

public class OutRootClass extends OutClass {

    private final Namer oGatewayNames = new Namer();
    private final DexFunctionDeclaration iFuncDecl;
    private int paramsCount;

    public OutRootClass(DexFunctionDeclaration iFuncDecl, OutShim oShim) {
        super((DexFile) iFuncDecl.getContainingFile(), iFuncDecl.getIdentifier().getNode().getText(), oShim);
        this.iFuncDecl = iFuncDecl;
        paramsCount = DexPsiImplUtil.getParamsCount(iFuncDecl.getSignature());
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
        List<DexParameterDeclaration> iParams = iSig.getParameters().getParameterDeclarationList();
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
            boolean isFirst = true;
            for (DexParameterDeclaration iParam : iParams) {
                for (DexParamDefinition iParamDef : iParam.getParamDefinitionList()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        oMethod.append(", ");
                    }
                    oMethod.append("Object ");
                    oMethod.append(iParamDef.getIdentifier());
                }
            }
            oMethod.append(") {");
            oMethod.indent(() -> {
                iFuncDecl.getBlock().acceptChildren(oMethod);
            });
            oMethod.appendNewLine('}');
            genClassBody();
            addBoat();
        });
        append('}');
        appendNewLine();
    }

    private void addBoat() {
        addBoatApply();
        addBoatCheck();
        Pier pier = new Pier(className(), paramsCount);
        Boat boat = new Boat(pier, qualifiedClassName(), "create__");
        oShim().addBoat(boat);
    }

    private void addBoatCheck() {
        String checkFuncName = oGatewayNames.giveName("can__create__");
        append("public static boolean ");
        append(checkFuncName);
        append('(');
        appendParamsDeclaration(paramsCount);
        appendNewLine(") {");
        appendNewLine("  return true;");
        appendNewLine("}");
    }

    private void addBoatApply() {
        String applyFuncName = oGatewayNames.giveName("create__");
        append("public static Result ");
        append(applyFuncName);
        append('(');
        appendParamsDeclaration(paramsCount);
        appendNewLine(") {");
        append("  return new ");
        append(packageName());
        append('.');
        append(className());
        append('(');
        appendParamsInvocation(paramsCount);
        appendNewLine(");");
        appendNewLine("}");
    }
}
