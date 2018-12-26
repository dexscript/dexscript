# one_arg

```dexscript
Array<uint8>
```

* genericType
    * "Array"
* typeArgs
    * size
        * 1
    * [0]
        * "uint8"

# two_args

```dexscript
Array<uint8, uint16>
```

* genericType
    * "Array"
* typeArgs
    * size
        * 2
    * [0]
        * "uint8"
    * [1]
        * "uint16"

# nested_expansion

```dexscript
Array<Array<uint8>>
```
* typeArgs
    * size
        * 1
    * [0]
        * `Array<uint8>`
        * genericType
            * "Array"

# missing_type_arg_recover_by_comma

```dexscript
Array<, uint16>
```

```dexscript
Array<<error/>, uint16>
```

# missing_type_arg_recover_by_right_angle_bracket

```dexscript
Array<>
```

```dexscript
Array<<error/>>
```

# missing_comma_recover_by_blank

```dexscript
Array<uint8 uint8>
```

```dexscript
Array<uint8 <error/>uint8>
```