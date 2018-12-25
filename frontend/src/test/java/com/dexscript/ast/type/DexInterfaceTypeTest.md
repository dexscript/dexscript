# empty_interface

```dexscript
interface {}
```

* methods
    * size
        * 0
* functions
    * size
        * 0

# one_function

```dexscript
interface {
   ::Hello(msg: string): string
}
```

* methods
    * size
        * 0
* functions
    * size
        * 1
    * [0]
        * identifier
            * "Hello"

# one_method

```dexscript
interface {
   Hello(msg: string): string
}
```

* methods
    * size
        * 1
    * [0]
        * identifier
            * "Hello"
* functions
    * size
        * 0

# function_and_method

```dexscript
interface {
   Hello(msg: string): string
   ::Hello(msg: string): string
}
```

* methods
    * size
        * 1
    * [0]
        * identifier
            * "Hello"
* functions
    * size
        * 1
    * [0]
        * identifier
            * "Hello"

# invalid_method_name

```dexscript
interface {
   ?? Hello(msg: string): string
}
```

method name is invalid

```dexscript
interface {
   <error/>?? Hello(msg: string): string
}
```