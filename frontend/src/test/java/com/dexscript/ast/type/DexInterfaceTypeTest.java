package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInterfaceTypeTest {

    @Test
    public void empty_interface() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void compact_empty_interface() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void one_function() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void one_method() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void function_and_method() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void invalid_method_name() {
        TestFramework.assertObject(DexInterfaceType::$);
    }

    @Test
    public void interface_with_name() {
        TestFramework.assertObject(DexInterfaceType::$);
    }
}
