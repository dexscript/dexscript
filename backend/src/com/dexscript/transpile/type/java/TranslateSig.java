package com.dexscript.transpile.type.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.DType;

import java.lang.reflect.*;

public interface TranslateSig {
    static DexSig $(JavaTypes javaTypes, Constructor ctor) {
        StringBuilder sig = new StringBuilder();
        sig.append('(');
        boolean isFirst = true;
        isFirst = translateTypeVariables(javaTypes, sig, ctor.getDeclaringClass().getTypeParameters(), isFirst);
        isFirst = translateTypeVariables(javaTypes, sig, ctor.getTypeParameters(), isFirst);
        Type[] jParams = ctor.getGenericParameterTypes();
        for (int i = 0; i < jParams.length; i++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sig.append(", ");
            }
            sig.append("arg");
            sig.append(i);
            sig.append(": ");
            sig.append(translateType(javaTypes, jParams[i]));
        }
        sig.append("): ");
        sig.append(dTypeNameOf(ctor.getDeclaringClass()));
        return new DexSig(sig.toString());
    }

    static String translateType(JavaTypes javaTypes, Type jTypeObj) {
        if (jTypeObj instanceof Class) {
            DType dType = javaTypes.resolve((Class) jTypeObj);
            return dType.toString();
        } else if (jTypeObj instanceof TypeVariable) {
            TypeVariable jType = (TypeVariable) jTypeObj;
            return jType.getName();
        } else if (jTypeObj instanceof ParameterizedType) {
            ParameterizedType jType = (ParameterizedType) jTypeObj;
            String dRawType = translateType(javaTypes, jType.getRawType());
            StringBuilder dType = new StringBuilder(dRawType);
            dType.append('<');
            Type[] jTypeArgs = jType.getActualTypeArguments();
            for (int i = 0; i < jTypeArgs.length; i++) {
                if (i > 0) {
                    dType.append(", ");
                }
                Type jTypeArg = jTypeArgs[i];
                String dTypeArg = translateType(javaTypes, jTypeArg);
                dType.append(dTypeArg);
            }
            dType.append('>');
            return dType.toString();
        } else if (jTypeObj instanceof WildcardType) {
            return "interface{}";
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    static boolean translateTypeVariables(
            JavaTypes javaTypes, StringBuilder sig, TypeVariable[] typeVariables, boolean isFirst) {
        for (TypeVariable jTypeParam : typeVariables) {
            if (isFirst) {
                isFirst = false;
            } else {
                sig.append(", ");
            }
            sig.append('<');
            sig.append(jTypeParam.getName());
            sig.append(">: ");
            if (jTypeParam.getBounds().length > 1) {
                throw new UnsupportedOperationException("not implemented");
            }
            Type jBound = jTypeParam.getBounds()[0];
            sig.append(translateType(javaTypes, jBound));
        }
        return isFirst;
    }

    static String dTypeNameOf(Class clazz) {
        return clazz.getSimpleName() + "_" + System.identityHashCode(clazz);
    }
}
