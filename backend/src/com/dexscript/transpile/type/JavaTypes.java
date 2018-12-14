package com.dexscript.transpile.type;

import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.runtime.UInt8;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.BuiltinTypes;
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

public class JavaTypes {

    private final Map<String, Type> types = new HashMap<>();
    private final Map<Type, String> typeChecks = new HashMap<>();
    private final OutShim oShim;

    public JavaTypes(OutShim oShim) {
        this.oShim = oShim;
        add(String.class, BuiltinTypes.STRING);
        add(Long.class, BuiltinTypes.INT64);
        add(UInt8.class, BuiltinTypes.UINT8);
        add(Boolean.class, BuiltinTypes.BOOL);
    }

    public void add(Class clazz, Type type) {
        types.put(clazz.getCanonicalName(), type);
    }

    public void add(String className, Type type) {
        types.put(className, type);
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
            for (Map.Entry<String, Type> entry : types.entrySet()) {
                if (!targetType.isAssignableFrom(entry.getValue())) {
                    continue;
                }
                String className = entry.getKey();
                g.__("if (obj instanceof "
                    ).__(className
                    ).__(new Line(") { return true; }"));
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

    public List<Type> resolve(Class[] classes) {
        List<Type> resolved = new ArrayList<>();
        for (Class<?> clazz : classes) {
            resolved.add(resolve(clazz));
        }
        return resolved;
    }

    public Type resolve(Class clazz) {
        Type type = types.get(clazz.getCanonicalName());
        if (type == null) {
            throw new DexRuntimeException(clazz.getCanonicalName() + " has not been imported");
        }
        return type;
    }

    public Type tryResolve(Class clazz) {
        return types.get(clazz.getCanonicalName());
    }
}
