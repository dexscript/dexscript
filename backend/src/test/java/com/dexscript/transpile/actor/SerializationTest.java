package com.dexscript.transpile.actor;

import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class SerializationTest {

    @Test
    public void serialize() {
        TestTranspile.$("" +
                "function Hello() {\n" +
                "   helper := new Helper()\n" +
                "   order := new Order(helper)\n" +
                "   encoded := <- helper\n" +
                "   task := DecodeTask<string>(helper, encoded)\n" +
                "   resolve 'hello' -> task\n" +
                "}\n" +
                "function Helper(): string {\n" +
                "   await {\n" +
                "   case GetMessage(): string {\n" +
                "       return EncodeTask(GetMessage)\n" +
                "   }}\n" +
                "}\n" +
                "function Order(helper: Helper) {\n" +
                "   msg := helper.GetMessage()\n" +
                "   print(msg)\n" +
                "}\n");
    }
}
