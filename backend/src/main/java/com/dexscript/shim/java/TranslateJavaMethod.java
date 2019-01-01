package com.dexscript.shim.java;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.NamedType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.dexscript.shim.java.TranslateJavaCtor.appendMore;
import static com.dexscript.shim.java.TranslateJavaCtor.translateType;

public interface TranslateJavaMethod {
    static DexSig $(JavaTypes javaTypes, Class clazz, Method method) {
        StringBuilder sig = new StringBuilder();
        sig.append('(');
        boolean isFirst = true;
        Type[] jParams = method.getGenericParameterTypes();
        isFirst = appendMore(sig, isFirst);
        sig.append("self: ");
        sig.append(((NamedType) javaTypes.resolve(clazz)).qualifiedName());
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
