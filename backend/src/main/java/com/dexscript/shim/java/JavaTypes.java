package com.dexscript.shim.java;

import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.runtime.UInt8;
import com.dexscript.shim.OutShim;
import com.dexscript.type.core.*;
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
        add(Float.class, ts.FLOAT32);
        add(Double.class, ts.FLOAT64);
        add(String.class, ts.STRING);
        primitiveTypes.put(Object.class, ts.ANY);
        primitiveTypes.put(boolean.class, ts.BOOL);
        primitiveTypes.put(int.class, ts.INT32);
        primitiveTypes.put(long.class, ts.INT64);
        primitiveTypes.put(float.class, ts.FLOAT32);
        primitiveTypes.put(double.class, ts.FLOAT64);
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
        } else if (targetType instanceof IntegerLiteralType) {
            isF = genIntegerLiteral((IntegerLiteralType) targetType);
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
        return targetType instanceof CompositeType && ((CompositeType) targetType).functions().isEmpty();
    }

    private String genStringLiteral(StringLiteralType stringLiteralType) {
        String isF = allocateShim(stringLiteralType);
        Gen g = oShim.g();
        g.__("// is "
        ).__(new Line(stringLiteralType.toString())
        ).__("public static boolean "
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

    private String genIntegerLiteral(IntegerLiteralType integerLiteralType) {
        String isF = allocateShim(integerLiteralType);
        Gen g = oShim.g();
        g.__("// is "
        ).__(new Line(integerLiteralType.toString())
        ).__("public static boolean "
        ).__(isF
        ).__("(Object obj) {");
        g.__(new Indent(() -> {
            g.__("return ((Long)"
            ).__(integerLiteralType.literalValue()
            ).__('L'
            ).__(").equals(obj);");
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
        g.__("// is "
        ).__(new Line(targetType.toString())
        ).__("public static boolean "
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
            if (typeToString.contains("<") || typeToString.contains("[")) {
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
        DType type = types.get(clazz.getCanonicalName());
        if (type == null) {
            type = primitiveTypes.get(clazz);
        }
        if (type == null) {
            type = new JavaType(oShim, clazz);
            add(clazz, type);
        }
        return type;
    }
}
