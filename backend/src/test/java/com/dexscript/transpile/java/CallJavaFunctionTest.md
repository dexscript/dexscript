# call_java_function

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
