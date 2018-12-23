package com.dexscript.shim.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.DType;
import com.dexscript.type.NamedType;

import java.lang.reflect.*;

public interface TranslateSig {

    static DexSig $(JavaTypes javaTypes, Constructor ctor) {
        Class clazz = ctor.getDeclaringClass();
        StringBuilder sig = new StringBuilder();
        sig.append('(');
        boolean isFirst = true;
        isFirst = translateTypeVariables(javaTypes, sig, clazz.getTypeParameters(), isFirst);
        isFirst = translateTypeVariables(javaTypes, sig, ctor.getTypeParameters(), isFirst);
        Type[] jParams = ctor.getGenericParameterTypes();
        isFirst = appendMore(sig, isFirst);
        sig.append("clazz: '");
        sig.append(clazz.getSimpleName());
        sig.append("'");
        for (int i = 0; i < jParams.length; i++) {
            isFirst = appendMore(sig, isFirst);
            sig.append("arg");
            sig.append(i);
            sig.append(": ");
            sig.append(translateType(javaTypes, jParams[i]));
        }
        sig.append("): ");
        sig.append(dTypeNameOf(clazz));
        if (clazz.getTypeParameters().length > 0) {
            sig.append('<');
            for (int i = 0; i < clazz.getTypeParameters().length; i++) {
                if (i > 0) {
                    sig.append(", ");
                }
                TypeVariable jTypeParam = clazz.getTypeParameters()[i];
                sig.append(jTypeParam.getName());
            }
            sig.append('>');
        }
        return new DexSig(sig.toString());
    }

    static boolean appendMore(StringBuilder sig, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
        } else {
            sig.append(", ");
        }
        return isFirst;
    }

    static String translateType(JavaTypes javaTypes, Type jTypeObj) {
        if (jTypeObj instanceof Class) {
            Class jType = (Class) jTypeObj;
            DType dType = javaTypes.resolve(jType);
            if (dType instanceof NamedType) {
                return ((NamedType) dType).name();
            }
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
            Type[] upperBounds = ((WildcardType) jTypeObj).getUpperBounds();
            if (upperBounds.length == 1) {
                return translateType(javaTypes, upperBounds[0]);
            }
            return "interface{}";
        } else {
            return "interface{}";
        }
    }

    static boolean translateTypeVariables(
            JavaTypes javaTypes, StringBuilder sig, TypeVariable[] typeVariables, boolean isFirst) {
        for (TypeVariable jTypeParam : typeVariables) {
            isFirst = appendMore(sig, isFirst);
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
