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

# two_params

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public void sayHello(String arg0, Long arg1) {}

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello", String.class, Long.class);
    }
}
```

* `(self: some.java.pkg.SomeClass, arg0: string, arg1: int64): void`

# return_string

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public String sayHello() {
        return "";
    }

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello");
    }
}
```

* `(self: some.java.pkg.SomeClass): string`

# referenced_class_type_param

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass<T> {

    public T sayHello() {
        return null;
    }

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello");
    }
}
```

* `(self: some.java.pkg.SomeClass<T>): T`

# one_type_param

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public <T> void sayHello() {
    }

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello");
    }
}
```

* `(<T>: interface{}, self: some.java.pkg.SomeClass): void`

# referenced_method_type_param

```java
package some.java.pkg;

import java.lang.reflect.Method;

public class SomeClass {

    public <T> void sayHello(T arg0) {
    }

    public static Method method() throws Exception {
        return SomeClass.class.getMethod("sayHello", Object.class);
    }
}
```

* `(<T>: interface{}, self: some.java.pkg.SomeClass, arg0: T): void`
