package com.dexscript.shim.java;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.core.NamedType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static com.dexscript.shim.java.TranslateJavaCtor.*;

public interface TranslateJavaMethod {
    static DexSig $(JavaTypes javaTypes, Class clazz, Method method) {
        StringBuilder sig = new StringBuilder();
        sig.append('(');
        boolean isFirst = true;
        isFirst = translateTypeVariables(javaTypes, sig, method.getTypeParameters(), isFirst);
        Type[] jParams = method.getGenericParameterTypes();
        isFirst = appendMore(sig, isFirst);
        sig.append("self: ");
        sig.append(((NamedType) javaTypes.resolve(clazz)).qualifiedName());
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
        for (int i = 0; i < jParams.length; i++) {
            isFirst = appendMore(sig, isFirst);
            sig.append("arg");
            sig.append(i);
            sig.append(": ");
            sig.append(translateType(javaTypes, jParams[i]));
        }
        sig.append("): ");
        sig.append(translateType(javaTypes, method.getGenericReturnType()));
        return new DexSig(new Text(sig.toString()));
    }
}
