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
