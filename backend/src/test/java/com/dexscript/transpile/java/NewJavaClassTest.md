# normal_class

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
    New__(class: 'SomeClass'): interface{}
}
```

```dexscript
function Hello(): interface{} {
    return new SomeClass()
}
```

* "SomeClass"
* getClass
    * getName
        * "some.java.pkg.SomeClass"


# generic_class

```java
package some.java.pkg;

public class SomeClass<T> {
    @Override
    public String toString() {
        return "SomeClass";
    }
}
```

```dexscript
interface :: {
    New__(<T>: interface{}, class: 'SomeClass'): interface{}
}
```

```dexscript
function Hello(): interface{} {
    return new SomeClass<int64>()
}
```

* "SomeClass"
* getClass
    * getName
        * "some.java.pkg.SomeClass"
