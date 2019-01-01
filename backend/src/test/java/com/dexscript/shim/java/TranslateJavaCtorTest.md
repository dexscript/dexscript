# no_type_param_no_param

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass {

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor();
    }
}
```

* `(class: 'SomeClass'): some.java.pkg.SomeClass`


# one_param

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass {

    public SomeClass(String arg0) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(String.class);
    }
}
```

* `(class: 'SomeClass', arg0: string): some.java.pkg.SomeClass`

# two_params

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass {

    public SomeClass(String arg0, Long arg1) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(String.class, Long.class);
    }
}
```

* `(class: 'SomeClass', arg0: string, arg1: int64): some.java.pkg.SomeClass`