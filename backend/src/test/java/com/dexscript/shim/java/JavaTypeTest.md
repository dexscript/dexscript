# just_object

```java
package some.java.pkg;

public class SomeClass {
}
```

```dexscript
interface SomeInf {
    toString(): string
}
```

| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `some.java.pkg.SomeClass` |

# one_param

```java
package some.java.pkg;

public class SomeClass {
    public void sayHello(Long arg0) {}
}
```

```dexscript
interface SomeInf {
    sayHello(arg0: int64)
}
```

| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `some.java.pkg.SomeClass` |

# one_type_param

```java
package some.java.pkg;

public class SomeClass<T> {
    public void sayHello(Long arg0) {}
}
```

```dexscript
interface SomeInf {
    sayHello(arg0: int64)
}
```

| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `some.java.pkg.SomeClass<string>` |
