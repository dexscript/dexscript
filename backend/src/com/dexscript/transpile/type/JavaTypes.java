package com.dexscript.transpile.type;

import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.runtime.UInt8;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaTypes {

    private final Map<String, DType> types = new HashMap<>();
    private final Map<Class, DType> primitiveTypes = new HashMap<>();
    private final Map<DType, String> typeChecks = new HashMap<>();
    private final OutShim oShim;

    public JavaTypes(OutShim oShim) {
        this.oShim = oShim;
        TypeSystem ts = oShim.typeSystem();
        add(Boolean.class, ts.BOOL);
        add(UInt8.class, ts.UINT8);
        add(Integer.class, ts.INT32);
        add(Long.class, ts.INT64);
        add(String.class, ts.STRING);
        primitiveTypes.put(boolean.class, ts.BOOL);
        primitiveTypes.put(int.class, ts.INT32);
        primitiveTypes.put(long.class, ts.INT64);
        primitiveTypes.put(void.class, ts.VOID);
    }

    public void add(Class clazz, DType type) {
        types.put(clazz.getCanonicalName(), type);
    }

    public void add(String className, DType type) {
        types.put(className, type);
    }

    public String genTypeCheck(DType targetType) {
        String typeCheck = typeChecks.get(targetType);
        if (typeCheck != null) {
            return typeCheck;
        }
        String isF;
        if (targetType instanceof StringLiteralType) {
            isF = genStringLiteral((StringLiteralType) targetType);
        } else if (isAny(targetType)) {
            isF = genAny(targetType);
        } else {
            isF = genGeneral(targetType);
        }
        typeChecks.put(targetType, isF);
        return isF;
    }

    private boolean isAny(DType targetType) {
        if (targetType instanceof AnyType) {
            return true;
        }
        return targetType instanceof FunctionsType && ((FunctionsType) targetType).functions().isEmpty();
    }

    private String genStringLiteral(StringLiteralType stringLiteralType) {
        String isF = allocateShim(stringLiteralType);
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

    private String genGeneral(DType targetType) {
        String isF = allocateShim(targetType);
        Gen g = oShim.g();
        g.__("// is "
        ).__(new Line(targetType.toString())
        ).__("public static boolean "
        ).__(isF
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__(new Line("Class clazz = obj.getClass();"));
            for (Map.Entry<String, DType> entry : types.entrySet()) {
                if (!IsAssignable.$(targetType, entry.getValue())) {
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

    private String genAny(DType targetType) {
        String isF = allocateShim(targetType);
        Gen g = oShim.g();
        g.__("public static boolean "
        ).__(isF
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__(new Line("return true;"));
        }));
        g.__(new Line("}"));
        return isF;
    }

    private String allocateShim(DType type) {
        if (type instanceof NamedType) {
            String typeToString = type.toString();
            if (typeToString.contains("<")) {
                return oShim.allocateShim("is__" + md5(typeToString));
            }
            return oShim.allocateShim("is__" + typeToString);
        }
        return oShim.allocateShim("is__" + md5(type.toString()));
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

    public List<DType> resolve(Class[] classes) {
        List<DType> resolved = new ArrayList<>();
        for (Class<?> clazz : classes) {
            resolved.add(resolve(clazz));
        }
        return resolved;
    }

    public DType resolve(Class clazz) {
        DType type = tryResolve(clazz);
        if (type == null) {
            throw new DexRuntimeException(clazz.getCanonicalName() + " has not been imported");
        }
        return type;
    }

    public DType tryResolve(Class clazz) {
        DType type = types.get(clazz.getCanonicalName());
        if (type == null) {
            type = primitiveTypes.get(clazz);
        }
        return type;
    }
}
