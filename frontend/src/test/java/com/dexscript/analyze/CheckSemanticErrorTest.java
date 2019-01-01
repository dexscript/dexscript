package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class CheckSemanticErrorTest {

    @Test
    public void reference_not_existing_variable() {
        hasSemanticError();
    }

    @Test
    public void reference_not_existing_type() {
        hasSemanticError();
    }

    @Test
    public void return_type_mismatch() {
        hasSemanticError();
    }

    @Test
    public void call_not_existing_function() {
        hasSemanticError();
    }

    @Test
    public void new_not_existing_function() {
        hasSemanticError();
    }

    @Test
    public void assign_type_mismatch() {
        hasSemanticError();
    }

    @Test
    public void interface_not_implemented() {
        hasSemanticError();
    }

    @Test
    public void interface_referenced_not_existing_type_parameter() {
        hasSemanticError();
    }

    @Test
    public void interface_referenced_existing_type_parameter() {
        noSemanticError();
    }

    @Test
    public void interface_method_referenced_its_type_parameter() {
        noSemanticError();
    }

    @Test
    public void function_referenced_not_existing_type_parameter() {
        hasSemanticError();
    }

    @Test
    public void function_referenced_existing_type_parameter() {
        noSemanticError();
    }

    private static void hasSemanticError() {
        Assert.assertTrue(check(testDataFromMySection().code()));
    }

    private static void noSemanticError() {
        Assert.assertFalse(check(testDataFromMySection().code()));
    }

    private static boolean check(String src) {
        TypeSystem ts = new TypeSystem();
        DexFile file = DexFile.$(src);
        file.attach(DexPackage.DUMMY);
        for (DexTopLevelDecl decl : file.topLevelDecls()) {
            if (decl.inf() != null) {
                ts.defineInterface(decl.inf());
            }
        }
        CheckSemanticError result = new CheckSemanticError(ts, file);
        return result.hasError();
    }
}
