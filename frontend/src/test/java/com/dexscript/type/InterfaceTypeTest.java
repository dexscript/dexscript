package com.dexscript.type;

import com.dexscript.ast.DexActor;
import org.junit.Before;
import org.junit.Test;

public class InterfaceTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    private void func(String src) {
        DexActor actor = DexActor.$("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType function = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret());
        function.implProvider(expandedFunc -> new Object());
    }

    @Test
    public void assignable_to_same_structure() {
        TestAssignable.$();
    }

    @Test
    public void more_member() {
        TestAssignable.$();
    }

    @Test
    public void argument_is_sub_type() {
        TestAssignable.$();
    }

    @Test
    public void ret_is_sub_type() {
        TestAssignable.$();
    }

    @Test
    public void implement_interface_by_define_function() {
        TestAssignable.$();
    }

    @Test
    public void argument_is_super_type_can_still_implement() {
        TestAssignable.$();
    }
}
