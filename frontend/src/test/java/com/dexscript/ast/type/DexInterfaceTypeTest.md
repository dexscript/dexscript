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

# Syntax Error

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
````