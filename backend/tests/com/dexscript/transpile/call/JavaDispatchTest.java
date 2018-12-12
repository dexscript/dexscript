package com.dexscript.transpile.call;

import com.dexscript.runtime.GenericBox;
import com.dexscript.runtime.UInt8;
import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JavaDispatchTest {

    @Test
    public void new_java_class() {
        GenericBox<List<UInt8>> box = (GenericBox<List<UInt8>>) Transpile.$("" +
                "function Hello(): List<UInt8> {\n" +
                "   list := new ArrayList<UInt8>()\n" +
                "   return list\n" +
                "}");
        Assert.assertEquals(0, box.get().size());
    }
}
