package com.dexscript.transpile.type;

import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.NamedType;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.Type;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeCandidates {

    private final List<TypeCandidate> candidates = new ArrayList<>();
    private final Map<Type, String> typeChecks = new HashMap<>();
    private final OutShim oShim;

    public TypeCandidates(OutShim oShim) {
        this.oShim = oShim;
        add(BuiltinTypes.STRING);
        add(BuiltinTypes.INT64);
        add(BuiltinTypes.UINT8);
    }

    private TypeCandidates add(Type type) {
        return add(new TypeCandidate(type.javaClassName(), false, type));
    }

    public TypeCandidates add(TypeCandidate candidate) {
        candidates.add(candidate);
        return this;
    }

    public String genTypeCheck(Type targetType) {
        String typeCheck = typeChecks.get(targetType);
        if (typeCheck != null) {
            return typeCheck;
        }
        String isF;
        if (targetType instanceof StringLiteralType) {
            isF = genStringLiteral((StringLiteralType) targetType);
        } else {
            isF = genGeneral(targetType);
        }
        typeChecks.put(targetType, isF);
        return isF;
    }

    private String genStringLiteral(StringLiteralType stringLiteralType) {
        String isF = oShim.allocateShim("is__" + md5(stringLiteralType.toString()));
        Gen g = oShim.g();
        g.__("public static boolean "
        ).__(isF
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__("return "
            ).__('"'
            ).__(stringLiteralType.literalValue()
            ).__('"'
            ).__(".equals(obj);");
        }));
        g.__(new Line("}"));
        return isF;
    }

    private String genGeneral(Type targetType) {
        String isF = oShim.allocateShim("is__" + targetType.toString());
        Gen g = oShim.g();
        g.__("public static boolean "
        ).__(isF
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__(new Line("Class clazz = obj.getClass();"));
            for (TypeCandidate candidate : candidates) {
                if (!targetType.isAssignableFrom(candidate.type())) {
                    continue;
                }
                if (candidate.isInterface()) {
                    g.__("if (obj instanceof "
                    ).__(candidate.javaClassName()
                    ).__(new Line(") { return true; }"));
                } else  {
                    g.__("if (clazz.equals("
                    ).__(candidate.javaClassName()
                    ).__(new Line(".class)) { return true; }"));
                }
            }
            g.__(new Line("return false;"));
        }));
        g.__(new Line("}"));
        return isF;
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
