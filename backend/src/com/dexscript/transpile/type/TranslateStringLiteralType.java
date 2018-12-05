package com.dexscript.transpile.type;

import com.dexscript.ast.core.DexParseException;
import com.dexscript.transpile.DexTranspileException;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.Type;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class TranslateStringLiteralType implements TranslateType {

    @Override
    public String handle(Gen g, Type type, List<Class> javaClasses, List<Class> javaInterfaces) {
        StringLiteralType stringLiteralType = (StringLiteralType) type;
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
            throw new DexTranspileException(e);
        }
    }
}
