package com.dexscript.runtime;

// new XXX<T> will create a object with generic type arguments
// however, we are NOT using java generics to do runtime multi dispatching
// if one argument is assignable to some parameter is determined by a pre-calculated dispatch table
// if (obj instanceof XXX) then dispatch-able
// new XXX<T> is also implemented this way, XXX is a class, XXX<T> is just a class XXX_T inherited from XXX
// we can use if (obj instanceof XXX_T) to know the argument is compatible
// for some final class we can not create sub class to new instance, we use this GenericBox to box it
// every generic type expansion will codegen a new subclass of GenericBox to carry the type information
public class GenericBox {

    private Object obj;

    public void set(Object obj) {
        this.obj = obj;
    }

    public Object get() {
        return obj;
    }
}
