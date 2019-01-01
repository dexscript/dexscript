# just_object

```java
package some.java.pkg;

public class SomeClass {
}
```

```dexscript
interface SomeClass {
    toString(): string
}
```


# one_param

```java
package some.java.pkg;

public class SomeClass {
    public void sayHello(Long arg0) {}
}
```

```dexscript
interface SomeClass {
    sayHello(arg0: int64)
}
```
