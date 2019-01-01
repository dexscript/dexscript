# no_type_param_no_param

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("toString");
    }
}
```

* `(self: some.java.pkg.SomeClass): string`

# one_param

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public void sayHello(Long arg0) {}

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello", Long.class);
    }
}
```

* `(self: some.java.pkg.SomeClass, arg0: int64): void`