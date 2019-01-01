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

# return_void

```dexscript
(): void
```

* params
    * size
        * 0
* typeParams
    * size
        * 0
* ret
    * "void"

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

# invalid_param_name_recover_by_comma

```dexscript
(msg?:string, msg2:string)
```

```dexscript
(<error/>msg?:string, msg2:string)
```

# invalid_param_name_recover_by_right_paren

```dexscript
(msg?:string)a
```

```dexscript
(<error/>msg?:string)
```

# invalid_param_name_recover_by_line_end

```dexscript
(msg?:string
a
```

```dexscript
(<error/>msg?:string
```

# invalid_param_name_recover_by_file_end

```dexscript
(msg?:string
```

```dexscript
(<error/>msg?:string
```

# param_name_missing_colon

```dexscript
(msg string, msg2:string)
```

```dexscript
(<error/>msg string, msg2:string)
```

# param_name_missing_type

```dexscript
(msg:, msg2:string)
```

* params
    * size
        * 2
    * [0]
        * `msg:<error/>`
    * [1]
        * "msg2:string"

# missing_right_paren_recover_by_file_end

```dexscript
(msg:string
```

```dexscript
(msg:string<error/>
```

# missing_right_paren_recover_by_line_end

```dexscript
(msg:string
a
```

```dexscript
(msg:string<error/>
```

# missing_return_type

```dexscript
():?
```

```dexscript
():<error/>
```

















