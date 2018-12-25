package com.dexscript.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface JavaSuperTypeArgs {

    static Type[] $(Class clazz) {
        Type[] interfaces = clazz.getGenericInterfaces();
        if (interfaces.length > 0) {
            return ((ParameterizedType) interfaces[0]).getActualTypeArguments();
        }
        Type superClass = clazz.getGenericSuperclass();
        return ((ParameterizedType) superClass).getActualTypeArguments();
    }
}
