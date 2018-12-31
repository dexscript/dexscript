# static_dispatch

```dexscript
function Hello(): string {
    return World('hello world')
}
function World(msg: int64): int64 {
    return msg
}
function World(msg: string): string {
    return msg
}
```

* "hello world"

# runtime_dispatch

```dexscript
function Hello(): string {
    var msg: string
    msg = 'world2'
    return World(msg)
}
function World(msg: 'world'): string {
    return 'yes, world'
}
function World(msg: string): string {
    return 'no, no'
}
```

* "no, no"

