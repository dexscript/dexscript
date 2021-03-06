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

# one_class_type_param

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

# argument_referenced_class_type_param

```java
package some.java.pkg;

public class SomeClass<T> {
    public void sayHello(T arg0) {}
}
```

```dexscript
interface SomeInf {
    sayHello(arg0: int64)
}
```

| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `some.java.pkg.SomeClass<int64>` |

# one_method_type_param

```java
package some.java.pkg;

public class SomeClass {
    public <T extends Long> void sayHello(T arg0) {}
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

# return_parameterized_self

```java
package some.java.pkg;

public class SomeClass<T> {
    public SomeClass<T> sayHello() {
        return null;
    }
}
```

```dexscript
interface SomeInf {
    sayHello(): SomeInf
}
```

| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `some.java.pkg.SomeClass<int64>` |

# one_dimension_array

```dexscript
interface SomeInf {
    Set__(index: int32, element: string)
    Get__(index: int32): string
}
```


| assignable | to        | from                      |
| ---------- | --------- | ------------------------- |
| true       | `SomeInf` | `java.lang.String_array` |
