package com.dexscript.transpiler;

import com.dexscript.psi.*;

import java.util.List;

public class OutRootClass extends OutClass {

    public OutRootClass(DexFile iFile, String packageName, DexFunctionDeclaration iFuncDecl) {
        super(iFile, packageName, iFuncDecl.getIdentifier().getNode().getText());
        appendNewLine("import com.dexscript.runtime.*;");
        appendNewLine();
        appendSourceLine(iFuncDecl.getFunction());
        append("public class ");
        append(iFuncDecl.getIdentifier());
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
        });
        append('}');
        appendNewLine();
    }
}
