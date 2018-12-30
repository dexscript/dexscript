# assign_string_const_type_to_literal_type

```dexscript
function Hello(): 'hello' {
   var msg: 'hello'
   msg = 'hello'
   return msg
}
```

* "hello"

# assign_string_const_type_to_string_type

```dexscript
function Hello(): string {
   var msg: string
   msg = 'hello'
   return msg
}
```

* "hello"

# assign_bool_const_type_to_literal_type

```dexscript
function Hello(): true {
   var msg: true
   msg = true
   return msg
}
```

* true

# assign_bool_const_type_to_bool

```dexscript
function Hello(): bool {
   var msg: true
   msg = true
   return msg
}
```

* true

# assign_integer_const_type_to_literal_type

```dexscript
function Hello(): 1 {
   var msg: 1
   msg = 1
   return msg
}
```

* 1
* getClass
    * getSimpleName
        * "Long"

# assign_integer_const_type_to_int64

```dexscript
function Hello(): int64 {
   var msg: int64
   msg = 1
   return msg
}
```

* 1
* getClass
    * getSimpleName
        * "Long"

# assign_integer_const_type_to_int32

```dexscript
function Hello(): int32 {
   var msg: int32
   msg = 1
   return msg
}
```

* 1
* getClass
    * getSimpleName
        * "Integer"

# assign_integer_const_type_to_float64

```dexscript
function Hello(): float64 {
   var msg: float64
   msg = 1
   return msg
}
```

* 1
* getClass
    * getSimpleName
        * "Double"

# assign_integer_const_type_to_float32

```dexscript
function Hello(): float32 {
   var msg: float32
   msg = 1
   return msg
}
```

* 1
* getClass
    * getSimpleName
        * "Float"

# assign_float_const_type_to_float64

```dexscript
function Hello(): float64 {
   var msg: float64
   msg = 1.1
   return msg
}
```

* "1.1"
* getClass
    * getSimpleName
        * "Double"

# assign_float_const_type_to_float32

```dexscript
function Hello(): float32 {
   var msg: float32
   msg = 1.1
   return msg
}
```

* "1.1"
* getClass
    * getSimpleName
        * "Float"





