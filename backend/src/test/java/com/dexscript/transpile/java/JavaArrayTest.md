# get_set_array_element

```java
package some.java.pkg;

public class SomeClass {
    public static String[] CreateOneElementArray() {
        return new String[1];
    }
}
```

```dexscript
interface :: {
    CreateOneElementArray(): OneElementArray
}

interface OneElementArray {
    Set__(index: int32, element: string)
    Get__(index: int32): string
}
```

```dexscript
function Hello(): string {
    array := CreateOneElementArray()
    array[0] = 'hello'
    return array[0]
}
```

* "hello"

# new_array_of_interface

```dexscript
interface :: {
}
```

```dexscript
function Hello(): string {
    array := new Array<string>(1)
    array[0] = 'hello'
    return array[0]
}
```