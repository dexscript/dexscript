package com.dexscript.transpile.type.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.DType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public interface TranslateSig {
    static DexSig $(JavaTypes javaTypes, Constructor ctor) {
        StringBuilder sig = new StringBuilder();
        sig.append('(');
        Type[] jParams = ctor.getGenericParameterTypes();
        for (int i = 0; i < jParams.length; i++) {
            if (i > 0) {
                sig.append(", ");
            }
            sig.append("arg");
            sig.append(i);
            sig.append(": ");
            Type jParam = jParams[i];
            if (jParam instanceof Class) {
                DType dParam = javaTypes.resolve((Class) jParam);
                sig.append(dParam.toString());
            } else {
                throw new UnsupportedOperationException("not implemented");
            }
        }
        sig.append("): ");
        sig.append(dtypeNameOf(ctor.getDeclaringClass()));
        return new DexSig(sig.toString());
    }

    static String dtypeNameOf(Class clazz) {
        return clazz.getSimpleName() + "_" + System.identityHashCode(clazz);
    }
}
