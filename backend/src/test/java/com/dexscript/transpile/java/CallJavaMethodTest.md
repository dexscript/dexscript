# call_java_method

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
