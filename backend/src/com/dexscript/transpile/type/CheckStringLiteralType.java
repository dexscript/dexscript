package com.dexscript.transpile.type;

import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.StringLiteralType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CheckStringLiteralType implements CheckType<StringLiteralType> {

    @Override
    public String handle(Gen g, StringLiteralType stringLiteralType, List<Class> javaClasses, List<Class> javaInterfaces) {
        String typeCheck = "is__" + md5(stringLiteralType.literalValue());
        g.__("public static boolean "
        ).__(typeCheck
        ).__(new Line("(Object obj) {"));
        g.__(new Indent(() -> {
            g.__("return "
            ).__('"'
            ).__(stringLiteralType.literalValue()
            ).__('"'
            ).__(".equals(obj);");
        }));
        g.__(new Line("}"));
        return typeCheck;
    }

    @NotNull
    private static String md5(String mySrc) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(mySrc.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new DexRuntimeException(e);
        }
    }
}
