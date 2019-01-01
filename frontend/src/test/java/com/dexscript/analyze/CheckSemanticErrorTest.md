# reference_not_existing_variable

```dexscript
function Hello() {
   return msg
}
```

# reference_not_existing_type

```dexscript
function Hello(msg: int10) {
}
```

# return_type_mismatch

```dexscript
function Hello(): string {
    return 1
}
```

# call_not_existing_function

```dexscript
function Hello() {
    world()
}
```

# new_not_existing_function

```dexscript
function Hello() {
    new world()
}
```

# assign_type_mismatch

```dexscript
function Hello() {
   var i: int64
   i = 'hello'
}
```

# interface_not_implemented

```dexscript
interface MyObject {
   Say(): string
}
function Hello() {
   var i: MyObject
   i = 'hello'
}
```

# interface_referenced_not_existing_type_parameter

```dexscript
interface List {
   <E>: interface{}
   get(index: int32): T
}
```

# interface_referenced_existing_type_parameter

```dexscript
interface List {
   <E>: interface{}
   get(index: int32): E
}
```

# interface_method_referenced_its_type_parameter

```dexscript
interface List {
   get(<E>: interface{}, index: int32): E
}
```

# function_referenced_not_existing_type_parameter

```dexscript
function Hello(<E>: interface{}, msg: T) {
}
```

# function_referenced_existing_type_parameter

```dexscript
function Hello(<E>: interface{}, msg: E) {
}
```







