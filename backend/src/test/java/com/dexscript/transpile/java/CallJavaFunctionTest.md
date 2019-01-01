# normal_function

```java
package some.java.pkg;

public class SomeClass {
    public static Long Add(Long left, Long right) {
        return left.longValue() + right.longValue();
    }
}
```

```dexscript
interface :: {
    Add(left: int64, right: int64): int64
}
```


```dexscript
function Hello(): int64 {
    return Add(1, 2)
}
```

* 3

# generic_function

```java
package some.java.pkg;

public class SomeClass {
    public static <T> T Add(T leftObj, T rightObj) {
        Long left = (Long)leftObj;
        Long right = (Long)rightObj;
        Long result = left.longValue() + right.longValue();
        return (T)result;
    }
}
```

```dexscript
interface :: {
    Add(<T>: interface{}, left: T, right: T): T
}
```


```dexscript
function Hello(): int64 {
    return Add(1, 2)
}
```

* 3

