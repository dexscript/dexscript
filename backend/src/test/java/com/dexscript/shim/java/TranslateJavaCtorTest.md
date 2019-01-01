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

# one_type_param

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass<T1> {

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor();
    }
}
```

* `(<T1>: interface{}, class: 'SomeClass'): some.java.pkg.SomeClass<T1>`

# type_param_with_bound

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass<T1 extends Long> {

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor();
    }
}
```

* `(<T1>: int64, class: 'SomeClass'): some.java.pkg.SomeClass<T1>`

# two_type_params

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass<T1, T2> {

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor();
    }
}
```

* `(<T1>: interface{}, <T2>: interface{}, class: 'SomeClass'): some.java.pkg.SomeClass<T1, T2>`

# type_param_from_ctor

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass {

    public <T1> SomeClass() {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor();
    }
}
```

* `(<T1>: interface{}, class: 'SomeClass'): some.java.pkg.SomeClass`

# param_ref_class_type_var

```java
package some.java.pkg;

import java.lang.reflect.Constructor;

public class SomeClass<T1> {

    public SomeClass(T1 arg0) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(Object.class);
    }
}
```

* `(<T1>: interface{}, class: 'SomeClass', arg0: T1): some.java.pkg.SomeClass<T1>`

# param_ref_type_var_expansion

```java
package some.java.pkg;

import java.lang.reflect.Constructor;
import java.util.List;

public class SomeClass<T1> {

    public SomeClass(List<T1> arg0) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(List.class);
    }
}
```

* `(<T1>: interface{}, class: 'SomeClass', arg0: java.util.List<T1>): some.java.pkg.SomeClass<T1>`

# param_ref_wildcard

```java
package some.java.pkg;

import java.lang.reflect.Constructor;
import java.util.List;

public class SomeClass {

    public SomeClass(List<?> arg0) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(List.class);
    }
}
```

* `(class: 'SomeClass', arg0: java.util.List<interface{}>): some.java.pkg.SomeClass`

# param_ref_wildcard_extends

```java
package some.java.pkg;

import java.lang.reflect.Constructor;
import java.util.List;

public class SomeClass<E> {

    public SomeClass(List<? extends E> arg0) {
    }

    public static Constructor ctor() throws Exception {
        return SomeClass.class.getConstructor(List.class);
    }
}
```

* `(<E>: interface{}, class: 'SomeClass', arg0: java.util.List<E>): some.java.pkg.SomeClass<E>`
