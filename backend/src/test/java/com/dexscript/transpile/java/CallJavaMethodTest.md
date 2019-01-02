# normal_method

```java
package some.java.pkg;

public class SomeClass {
    @Override
    public String toString() {
        return "SomeClass";
    }
}
```

```dexscript
interface :: {
    New__(class: 'SomeClass'): SomeInf
}
interface SomeInf {
    toString(): string
}
```

```dexscript
function Hello(): string {
    return new SomeClass().toString()
}
```

* "SomeClass"
* getClass
    * getName
        * "java.lang.String"


# generic_method_referenced_class_type_param


```java
package some.java.pkg;

public class SomeClass<T> {

    private T val;

    public SomeClass(T val) {
        this.val = val;
    }

    public T get() {
        return val;
    }
}
```

```dexscript
interface :: {
    New__(<T>: interface{}, class: 'SomeClass', val: T): SomeInf<T>
}
interface SomeInf {
    <T>: interface{}
    get(): T
}
```

```dexscript
function Hello(): int64 {
    box := new SomeClass(100)
    return box.get()
}
```

* "100"
* getClass
    * getName
        * "java.lang.Long"