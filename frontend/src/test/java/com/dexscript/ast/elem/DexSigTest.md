# one_param

```dexscript
(msg:string)
```

* params
    * size
        * 1
    * [0]
        * "msg:string"
* typeParams
    * size
        * 0
* ret
    * "void"

# empty

```dexscript
(){}
```

```dexscript
()
```

* params
    * size
        * 0
* typeParams
    * size
        * 0
* ret
    * "void"

# two_params

```dexscript
(msg1:string, msg2:string)
```

* params
    * size
        * 2
    * [0]
        * "msg1:string"
    * [1]
        * "msg2:string"
* typeParams
    * size
        * 0
* ret
    * "void"

# return_value

```dexscript
() : string
```

* params
    * size
        * 0
* typeParams
    * size
        * 0
* ret
    * "string"

# one_type_param

```dexscript
(<T>: string): T
```

* params
    * size
        * 0
* typeParams
    * size
        * 1
    * [0]
        * `<T>: string`
* ret
    * "T"

# two_type_params

```dexscript
(<T>: string, <T2>: string): T
```

* params
    * size
        * 0
* typeParams
    * size
        * 2
    * [0]
        * `<T>: string`
    * [1]
        * `<T2>: string`
* ret
    * "T"





