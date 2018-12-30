# method_call_is_same_as_function_call

```dexscript
function Hello(): string {
    return 'hello world'.World()
}
function World(msg: string): string {
    return msg
}
```

* "hello world"
