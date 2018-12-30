# call_without_arg

```dexscript
function Hello(): string {
    return World()
}
function World(): string {
    return 'hello world'
}
```

* "hello world"

# call_with_pos_arg

```dexscript
function Hello(): string {
    return World('hello world')
}
function World(msg: string): string {
    return msg
}
```

* "hello world"

# call_with_named_arg

```dexscript
function Hello(): string {
    return World(msg='hello world')
}
function World(msg: string): string {
    return msg
}
```

* "hello world"

# function_without_return_value

```dexscript
function Hello(): string {
    World()
    return 'hello world'
}
function World() {
}
```

* "hello world"

# invoke_int64

```dexscript
function Hello(): int64 {
    return World(100)
}
function World(arg: int64): int64 {
    return arg
}
```

* 100
* getClass
    * getSimpleName
        * "Long"

# invoke_int32

```dexscript
function Hello(): int32 {
    return World(100)
}
function World(arg: int32): int32 {
    return arg
}
```

* 100
* getClass
    * getSimpleName
        * "Integer"
