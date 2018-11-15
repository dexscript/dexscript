package com.dexscript.transpiler;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexFunctionDeclaration;

public class OutRootClass extends OutClass {

    public OutRootClass(DexFile iFile, String packageName, DexFunctionDeclaration iFuncDecl) {
        super(iFile, packageName, iFuncDecl.getIdentifier().getNode().getText());
        appendNewLine("import com.dexscript.runtime.Actor;");
        appendNewLine("import com.dexscript.runtime.Result;");
        appendNewLine("import com.dexscript.runtime.Result1;");
        appendNewLine();
        appendSourceLine(iFuncDecl.getFunction());
        append("public class ");
        append(iFuncDecl.getIdentifier());
        append(" extends Actor implements Result1 {");
        indent(() -> {
            appendNewLine();
            // oFields for return value
            appendReturnValueFields(iFuncDecl.getSignature());
            appendNewLine();
            // constructor
            OutMethod oMethod = new OutMethod(this, iFuncDecl.getSignature());
            oMethod.append("public ");
            oMethod.append(iFuncDecl.getIdentifier());
            oMethod.append("() {");
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
