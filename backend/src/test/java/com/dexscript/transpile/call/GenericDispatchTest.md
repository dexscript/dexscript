# call_generic_function_with_type_inference

```dexscript
function Hello(): string {
   return World('hello')
}
function World(<T>: string, msg: T): T {
   return msg
}
```

* "hello"

# call_generic_function_with_type_arg

```dexscript
function Hello(): string {
   return World<string>('hello')
}
function World(<T>: string, msg: T): T {
   return msg
}
```

* "hello"
